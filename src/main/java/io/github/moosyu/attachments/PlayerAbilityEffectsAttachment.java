package io.github.moosyu.attachments;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class PlayerAbilityEffectsAttachment {
    private final Map<Identifier, ActiveEffectEntry> activeEffects = new HashMap<>();
    private record ActiveEffectEntry(long expiryTime, Consumer<Player> onExpire) {}

    public void addActiveEffect(Identifier abilityIdentifier, long abilityLength, Level level, Consumer<Player> onExpire) {
        activeEffects.put(abilityIdentifier, new ActiveEffectEntry(level.getGameTime() + abilityLength, onExpire));
    }

    public void setActiveEffectExpiryTime(Identifier abilityIdentifier, long abilityLength, Level level, Consumer<Player> onExpire) {
        activeEffects.replace(abilityIdentifier, new ActiveEffectEntry(level.getGameTime() + abilityLength, onExpire));
    }

    public boolean hasActiveEffect(Identifier abilityIdentifier) {
        return activeEffects.containsKey(abilityIdentifier);
    }

    public boolean hasAnyActiveEffect() {
        return !activeEffects.isEmpty();
    }

    public boolean activeEffectFinished(Identifier abilityIdentifier, Level level) {
        if (!hasActiveEffect(abilityIdentifier)) return false;
        return level.getGameTime() > activeEffects.get(abilityIdentifier).expiryTime;
    }

    public void tickEffects(Level level, Player player) {
        Iterator<Map.Entry<Identifier, ActiveEffectEntry>> currentEffect = activeEffects.entrySet().iterator();
        while (currentEffect.hasNext()) {
            Map.Entry<Identifier, ActiveEffectEntry> entry = currentEffect.next();
            if (activeEffectFinished(entry.getKey(), level)) {
                entry.getValue().onExpire().accept(player);
                currentEffect.remove();
            }
        }
    }
}