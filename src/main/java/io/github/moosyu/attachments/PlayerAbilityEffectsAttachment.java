package io.github.moosyu.attachments;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class PlayerAbilityEffectsAttachment {
    private final Map<Identifier, ActiveEffectEntry> activeEffects = new HashMap<>();
    private record ActiveEffectEntry(long expiryTime, @Nullable Consumer<Player> onExpire, ItemStack itemStack) {}

    /**
     * @param abilityIdentifier identifier for the ability
     * @param abilityLength length of the ability in ticks
     * @param level server level
     * @param onExpire consumer to run when effect expires
     * @param itemStack itemstack being used
     */
    public void addActiveEffect(Identifier abilityIdentifier, long abilityLength, Level level, Consumer<Player> onExpire, ItemStack itemStack) {
        activeEffects.put(abilityIdentifier, new ActiveEffectEntry(level.getGameTime() + abilityLength, onExpire, itemStack));
    }

    /**
     * for adding not-item related active effects
     * @param abilityIdentifier identifier for the ability
     * @param abilityLength length of the ability in ticks
     * @param level server level
     * @param onExpire consumer to run when effect expires
     */
    public void addActiveEffect(Identifier abilityIdentifier, long abilityLength, Level level, Consumer<Player> onExpire) {
        activeEffects.put(abilityIdentifier, new ActiveEffectEntry(level.getGameTime() + abilityLength, onExpire, ItemStack.EMPTY));
    }


    public void removeActiveEffect(Identifier abilityIdentifier, Player player) {
        Consumer<Player> onExpireEvent = activeEffects.get(abilityIdentifier).onExpire;

        if (onExpireEvent != null) {
            onExpireEvent.accept(player);
        }
        activeEffects.remove(abilityIdentifier);
    }

    public void setActiveEffectExpiryTime(Identifier abilityIdentifier, long abilityLength, Level level, Consumer<Player> onExpire, ItemStack itemStack) {
        activeEffects.replace(abilityIdentifier, new ActiveEffectEntry(level.getGameTime() + abilityLength, onExpire, itemStack));
    }

    public boolean hasActiveEffect(Identifier abilityIdentifier) {
        return activeEffects.containsKey(abilityIdentifier);
    }

    public boolean hasAnyActiveEffect() {
        return !activeEffects.isEmpty();
    }

    public boolean activeEffectFinished(Identifier abilityIdentifier, Level level) {
        if (!hasActiveEffect(abilityIdentifier)) return false;
        // if expiry time is 0 effect should be removed manually
        return level.getGameTime() > activeEffects.get(abilityIdentifier).expiryTime() && activeEffects.get(abilityIdentifier).expiryTime() != 0;
    }

    public long expiryTimeTicks(Identifier abilityIdentifier) {
        return activeEffects.get(abilityIdentifier).expiryTime();
    }

    public void tickEffects(Level level, Player player) {
        Iterator<Map.Entry<Identifier, ActiveEffectEntry>> currentEffect = activeEffects.entrySet().iterator();
        while (currentEffect.hasNext()) {
            Map.Entry<Identifier, ActiveEffectEntry> entry = currentEffect.next();
            if (activeEffectFinished(entry.getKey(), level)) {
                if (entry.getValue().onExpire() != null) {
                    entry.getValue().onExpire().accept(player);
                }
                currentEffect.remove();
            }
        }
    }
}