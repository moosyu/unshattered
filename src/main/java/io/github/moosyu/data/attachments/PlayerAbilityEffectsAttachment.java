package io.github.moosyu.data.attachments;

import io.github.moosyu.abilities.AbilityContext;
import io.github.moosyu.abilities.AbilityContextKey;
import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.ItemTypes;
import io.github.moosyu.abilities.PassiveAbilityItem;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

public final class PlayerAbilityEffectsAttachment {
    private final Map<Identifier, ActiveEffectEntry> activeEffects = new HashMap<>();
    private record ActiveEffectEntry(long expiryTime, @Nullable Consumer<AbilityContext> onExpire) {}
    private final Map<PassiveAbilityItem, Boolean> storedPassiveOngoingItems = new HashMap<>();
    private final Set<PassiveAbilityItem> storedPassiveNonOngoingItems = new HashSet<>();
    private Float lockedAttackStrength = null;

    /**
     * @param abilityIdentifier identifier for the ability
     * @param abilityLength length of the ability in ticks
     * @param level server level
     * @param onExpire consumer to run when effect expires
     */
    public void addActiveEffect(Identifier abilityIdentifier, long abilityLength, Level level, Consumer<AbilityContext> onExpire) {
        activeEffects.put(abilityIdentifier, new ActiveEffectEntry(level.getGameTime() + abilityLength, onExpire));
    }

    /**
     * removes an active effect on the player (for when the effect is finished), removing it from active effects and running its onExpire consumer
     * @param abilityIdentifier identifier for the effect
     * @param player player having the effect removed
     */
    public void removeActiveEffect(Identifier abilityIdentifier, ServerPlayer player) {
        ActiveEffectEntry entry = activeEffects.remove(abilityIdentifier);
        if (entry == null) {
            return;
        }

        if (entry.onExpire() != null) {
            entry.onExpire().accept(new AbilityContext().add(AbilityContextKey.PLAYER, player));
        }
    }

    /**
     * checks whether player has an effect active
     * @param abilityIdentifier ability identifier
     * @return true if the player has it
     */
    public boolean hasActiveEffect(Identifier abilityIdentifier) {
        return activeEffects.containsKey(abilityIdentifier);
    }

    /**
     * @return true if the player has any active effects
     */
    public boolean hasAnyActiveEffect() {
        return !activeEffects.isEmpty();
    }

    /**
     * @param abilityIdentifier ability identifier
     * @param level level for game time
     * @return whether or not an active effect has been finished
     */
    public boolean activeEffectFinished(Identifier abilityIdentifier, Level level) {
        if (!hasActiveEffect(abilityIdentifier)) return false;
        // if expiry time is 0 effect should be removed manually
        return level.getGameTime() > activeEffects.get(abilityIdentifier).expiryTime() && activeEffects.get(abilityIdentifier).expiryTime() != 0;
    }

    /**
     * get ticks left until expiration or throws an exception if theres no effect for that identifier
     * @param abilityIdentifier the effect identifier (used to add the effect initially
     * @return the amount of time until effect is to expire
     */
    public long expiryTimeTicks(Identifier abilityIdentifier) {
        ActiveEffectEntry entry = activeEffects.get(abilityIdentifier);
        if (entry == null) {
            throw new IllegalArgumentException("no effect for " + abilityIdentifier);
        }

        return entry.expiryTime();
    }

    /**
     * adds a passive tick item (with active set to true) to map and triggers its ability
     */
    public void addPassiveItem(PassiveAbilityItem item, AbilityContext context) {
        if (item.triggerTypes().contains(AbilityTriggerType.ONGOING)) {
            if (!Boolean.TRUE.equals(storedPassiveOngoingItems.put(item, true)) && item.abilityConditionsMet(context)) {
                item.onAbilityTriggered(context);
            }
        } else {
            storedPassiveNonOngoingItems.add(item);
        }
    }

    /**
     * removes a passive ticked item from the set/map and if also runs onAbilityFinished
     */
    public void removePassiveItem(PassiveAbilityItem item, AbilityContext context) {
        if (item.triggerTypes().contains(AbilityTriggerType.ONGOING)) {
            if (storedPassiveOngoingItems.remove(item)) {
                item.onAbilityFinished(context);
            }
        } else {
            if (storedPassiveNonOngoingItems.remove(item)) {
                item.onAbilityFinished(context);
            }
        }
    }

    public Set<PassiveAbilityItem> getStoredPassiveNonOngoingItems() {
        return storedPassiveNonOngoingItems;
    }

    /**
     * tick player active effects and ensure passive item abilities are still having their conditions met
     */
    public void updateEffects(AbilityContext context, Level level) {
        Iterator<Map.Entry<Identifier, ActiveEffectEntry>> currentEffect = activeEffects.entrySet().iterator();
        while (currentEffect.hasNext()) {
            Map.Entry<Identifier, ActiveEffectEntry> entry = currentEffect.next();
            if (activeEffectFinished(entry.getKey(), level)) {
                if (entry.getValue().onExpire() != null) {
                    entry.getValue().onExpire().accept(context);
                }
                currentEffect.remove();
            }
        }

        for (Map.Entry<PassiveAbilityItem, Boolean> entry : storedPassiveOngoingItems.entrySet()) {
            PassiveAbilityItem item = entry.getKey();
            boolean active = entry.getValue();
            boolean conditionsMet = item.abilityConditionsMet(context);

            if (item.triggerTypes().contains(AbilityTriggerType.TICKED)) {
                if (conditionsMet) {
                    item.onAbilityTriggered(context);
                    entry.setValue(true);
                }
            } else {
                if (conditionsMet && !active) {
                    item.onAbilityTriggered(context);
                    entry.setValue(true);
                } else if (!conditionsMet && active) {
                    item.onAbilityFinished(context);
                    entry.setValue(false);
                }
            }
        }
    }

    /**
     * runs the ability finishes for active effects and passive ticked items, clearing the active effects map but not the storedPassiveOngoingItems set
     * (it needs to be serialized so the effects can be reapplied on rejoin)
     * @param player server player
     */
    public void forceStopEffects(ServerPlayer player) {
        AbilityContext context = new AbilityContext().add(AbilityContextKey.PLAYER, player);

        activeEffects.forEach((_, effect) -> {
            if (effect.onExpire() != null) {
                effect.onExpire().accept(context);
            }
        });

        storedPassiveOngoingItems.forEach((item, active) -> {
            if (active) {
                item.onAbilityFinished(context);
            }
        });

        storedPassiveNonOngoingItems.forEach(item -> item.onAbilityFinished(context));

        activeEffects.clear();
    }

    /**
     * apply passive ticked effects joining, does a full inventory and talisman bag check instead
     * of checking a codec to make sure nothing terrible occured in the last session.
     * @param player server player having effects reapplied
     */
    public void applyPassiveEffects(ServerPlayer player) {
        storedPassiveOngoingItems.clear();
        storedPassiveNonOngoingItems.clear();

        AbilityContext context = new AbilityContext().add(AbilityContextKey.PLAYER, player);
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack itemStack = player.getItemBySlot(slot);
            if (!itemStack.isEmpty() && itemStack.getItem() instanceof PassiveAbilityItem item) {
                if (item.triggerTypes().contains(AbilityTriggerType.ONGOING)) {
                    if (item.abilityConditionsMet(context)) {
                        item.onAbilityTriggered(context);
                        storedPassiveOngoingItems.put(item, true);
                    } else {
                        storedPassiveOngoingItems.put(item, false);
                    }
                } else {
                    storedPassiveNonOngoingItems.add(item);
                }
            }
        }

        player.getData(UnshatteredAttachments.PLAYER_TALISMAN_STORAGE.get()).forEach(itemStack -> {
            if (!itemStack.isEmpty() && itemStack.getItem() instanceof PassiveAbilityItem item && itemStack.get(UnshatteredDataComponents.ITEM_TYPE.get()) == ItemTypes.TALISMAN) {
                if (item.triggerTypes().contains(AbilityTriggerType.ONGOING)) {
                    if (item.abilityConditionsMet(context)) {
                        item.onAbilityTriggered(context);
                        storedPassiveOngoingItems.put(item, true);
                    } else {
                        storedPassiveOngoingItems.put(item, false);
                    }
                } else {
                    storedPassiveNonOngoingItems.add(item);
                }
            }
        });
    }

    /**
     * for things like cleave where the attack strengh cannot be updated until multiple hits have occurred
     */
    public float lockAttackStrength(Player player) {
        if (lockedAttackStrength == null) {
            lockedAttackStrength = player.getAttackStrengthScale(0.0f);
        }
        return lockedAttackStrength;
    }

    public boolean hasLockedAttackStrength() {
        return lockedAttackStrength != null;
    }

    public void resetLockedStrength() {
        lockedAttackStrength = null;
    }
}