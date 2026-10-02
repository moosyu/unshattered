package io.github.moosyu.data.attachments;

import io.github.moosyu.abilities.*;
import io.github.moosyu.data.components.ItemAttachments;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.ItemType;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

public final class PlayerAbilityEffectsAttachment {
    private record ActiveEffectEntry(long expiryTime, @Nullable Consumer<AbilityContext> onExpire) {}
    private final Map<Identifier, ActiveEffectEntry> activeEffects = new HashMap<>();
    // ability -> whether its ability is currently active, methods written in a way that player should only ever have one of the exact same ability items
    private final Map<PassiveAbilityItem, Boolean> storedPassiveOngoingItems = new HashMap<>();
    private final Set<ItemStack> storedNonOngoingItems = Collections.newSetFromMap(new IdentityHashMap<>());

    /**
     * @param abilityIdentifier identifier for the ability
     * @param abilityLength length of the ability in ticks
     * @param level server level
     * @param onExpire consumer to run when effect expires (aside from being removed from active effects)
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
     */
    public boolean hasActiveEffect(Identifier abilityIdentifier) {
        return activeEffects.containsKey(abilityIdentifier);
    }

    /**
     * @return whether an active effect has been finished
     */
    public boolean activeEffectFinished(Identifier abilityIdentifier, Level level) {
        ActiveEffectEntry entry = activeEffects.get(abilityIdentifier);
        return entry != null && isFinished(entry, level);
    }

    private static boolean isFinished(ActiveEffectEntry entry, Level level) {
        // if expiry time is 0 effect should be removed manually
        return entry.expiryTime() != 0 && level.getGameTime() > entry.expiryTime();
    }

    /**
     * @return the absolute game time at which the effect expires
     * @throws IllegalArgumentException if there is no effect for that identifier
     */
    public long expiryTimeTicks(Identifier abilityIdentifier) {
        ActiveEffectEntry entry = activeEffects.get(abilityIdentifier);
        if (entry == null) {
            throw new IllegalArgumentException("no effect for " + abilityIdentifier);
        }

        return entry.expiryTime();
    }

    /**
     * @return ticks remaining until the effect expires (never negative)
     * @throws IllegalArgumentException if there is no effect for that identifier
     */
    public long remainingTicks(Identifier abilityIdentifier, Level level) {
        return Math.max(0, expiryTimeTicks(abilityIdentifier) - level.getGameTime());
    }

    /**
     * ongoing passive items are tracked by type and triggered if their conditions are met.
     * non-ongoing passive items and all incremental items are tracked by itemstack so they can be found later (to add to increments etc)
     */
    public void addPassiveItem(ItemStack item, AbilityContext context) {
        if (item.isEmpty() || !(item.getItem() instanceof AbilityItem abilityItem)) {
            return;
        }

        if (abilityItem instanceof PassiveAbilityItem passiveAbilityItem) {
            if (abilityItem.triggerTypes().contains(AbilityTriggerType.ONGOING)) {
                addOngoing(passiveAbilityItem, context);
            } else {
                storedNonOngoingItems.add(item);
            }
        }

        if (abilityItem instanceof IncrementalAbilityItem) {
            storedNonOngoingItems.add(item);
        }
    }

    /**
     * removes an ability item, running onAbilityFinished for passive items that were active/tracked
     */
    public void removePassiveItem(ItemStack itemStack, AbilityContext context) {
        if (!(itemStack.getItem() instanceof AbilityItem abilityItem)) {
            return;
        }

        if (abilityItem instanceof PassiveAbilityItem passiveAbilityItem) {
            if (abilityItem.triggerTypes().contains(AbilityTriggerType.ONGOING)) {
                if (Boolean.TRUE.equals(storedPassiveOngoingItems.remove(abilityItem))) {
                    passiveAbilityItem.onAbilityFinished(context);
                }
            } else {
                if (removeTrackedStack(itemStack)) {
                    passiveAbilityItem.onAbilityFinished(context);
                }
            }
        }

        if (abilityItem instanceof IncrementalAbilityItem) {
            removeTrackedStack(itemStack);
        }
    }

    /**
     * read only view of stored non-ongoing items, add/remove through addPassiveItem/removePassiveItem
     */
    public Set<ItemStack> getStoredNonOngoingItems() {
        return Collections.unmodifiableSet(storedNonOngoingItems);
    }

    private void addOngoing(PassiveAbilityItem item, AbilityContext context) {
        // already tracked (e.g. login scan plus equipment events), don't trigger twice
        if (storedPassiveOngoingItems.containsKey(item)) {
            return;
        }

        boolean active = item.abilityConditionsMet(context);
        storedPassiveOngoingItems.put(item, active);

        if (active) {
            item.onAbilityTriggered(context);
        }
    }

    /**
     * tries to remove a itemStack, as itemstack identities seem to change often it falls back to a comparison
     * @return true if the itemStack was removed successfully
     */
    private boolean removeTrackedStack(ItemStack itemStack) {
        if (storedNonOngoingItems.remove(itemStack)) {
            return true;
        }

        return storedNonOngoingItems.removeIf(tracked -> tracked.getItem() == itemStack.getItem());
    }

    /**
     * tick player active effects and ensure passive item abilities are still having their conditions met
     */
    public void updateEffects(AbilityContext context, Level level) {
        List<Identifier> finished = new ArrayList<>();
        for (Map.Entry<Identifier, ActiveEffectEntry> entry : activeEffects.entrySet()) {
            if (isFinished(entry.getValue(), level)) {
                finished.add(entry.getKey());
            }
        }

        for (Identifier identifier : finished) {
            ActiveEffectEntry entry = activeEffects.remove(identifier);
            if (entry != null && entry.onExpire() != null) {
                entry.onExpire().accept(context);
            }
        }

        // DON'T REMOVE THE SNAPSHOT, ConcurrentModificationException's can happen without it
        for (PassiveAbilityItem item : new ArrayList<>(storedPassiveOngoingItems.keySet())) {
            Boolean active = storedPassiveOngoingItems.get(item);
            if (active == null) continue;

            boolean conditionsMet = item.abilityConditionsMet(context);

            if (item.triggerTypes().contains(AbilityTriggerType.TICKED)) {
                if (conditionsMet) {
                    item.onAbilityTriggered(context);
                    storedPassiveOngoingItems.put(item, true);
                }
            } else {
                if (conditionsMet && !active) {
                    item.onAbilityTriggered(context);
                    storedPassiveOngoingItems.put(item, true);
                } else if (!conditionsMet && active) {
                    item.onAbilityFinished(context);
                    storedPassiveOngoingItems.put(item, false);
                }
            }
        }
    }

    /**
     * apply passive effects on joining, does a full inventory and talisman bag check instead
     * of checking a codec to make sure nothing terrible occurred in the last session.
     */
    public void applyPassiveEffects(ServerPlayer player) {
        storedPassiveOngoingItems.clear();
        storedNonOngoingItems.clear();

        List<ItemStack> itemStacks = new ArrayList<>();
        AbilityContext context = new AbilityContext().add(AbilityContextKey.PLAYER, player);

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack itemStack = player.getItemBySlot(slot);
            if (!itemStack.isEmpty() && itemStack.getItem() instanceof AbilityItem) {
                ItemAttachments itemAttachments = itemStack.get(UnshatteredDataComponents.ITEM_ATTACHMENTS);

                if (itemAttachments != null) {
                    itemAttachments.attachments().forEach((_, attachmentItemStack) -> {
                        if (attachmentItemStack.getItem() instanceof AbilityItem) {
                            addPassiveItem(attachmentItemStack, context);
                        }
                    });
                }

                itemStacks.add(itemStack);
            }
        }

        player.getData(UnshatteredAttachments.PLAYER_TALISMAN_STORAGE.get()).forEach(itemStack -> {
            if (!itemStack.isEmpty()
                    && itemStack.getItem() instanceof AbilityItem
                    && itemStack.get(UnshatteredDataComponents.ITEM_TYPE.get()) == ItemType.TALISMAN) {
                itemStacks.add(itemStack);
            }
        });

        for (ItemStack itemStack : itemStacks) {
            addPassiveItem(itemStack, context);
        }
    }

    /**
     * runs onAbilityFinished for active effects and active passive items, clearing the active effects map
     * but keeping the tracked items (they're rescanned on rejoin via applyPassiveEffects)
     */
    public void forceStopEffects(ServerPlayer player) {
        AbilityContext context = new AbilityContext().add(AbilityContextKey.PLAYER, player);

        List<ActiveEffectEntry> effects = new ArrayList<>(activeEffects.values());
        activeEffects.clear();
        for (ActiveEffectEntry effect : effects) {
            if (effect.onExpire() != null) {
                effect.onExpire().accept(context);
            }
        }

        for (PassiveAbilityItem item : new ArrayList<>(storedPassiveOngoingItems.keySet())) {
            if (Boolean.TRUE.equals(storedPassiveOngoingItems.get(item))) {
                storedPassiveOngoingItems.put(item, false);
                item.onAbilityFinished(context);
            }
        }

        for (ItemStack itemStack : new ArrayList<>(storedNonOngoingItems)) {
            if (itemStack.getItem() instanceof PassiveAbilityItem passiveAbilityItem) {
                passiveAbilityItem.onAbilityFinished(context);
            }
        }
    }
}