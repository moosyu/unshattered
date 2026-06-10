package io.github.moosyu.attachments;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

public class PlayerAbilityEffectsAttachment {
    private final Map<Identifier, EffectEntry> activeEffects = new HashMap<>();

    public record EffectEntry(long expiryTime, Consumer<Player> onExpire) {}

    public void addEffect(Identifier abilityIdentifier, long abilityLength, Level level, Consumer<Player> onExpire) {
        activeEffects.put(abilityIdentifier, new EffectEntry(level.getGameTime() + abilityLength, onExpire));
    }

    public void setEffectExpiryTime(Identifier abilityIdentifier, long abilityLength, Level level, Consumer<Player> onExpire) {
        activeEffects.replace(abilityIdentifier, new EffectEntry(level.getGameTime() + abilityLength, onExpire));
    }

    public boolean hasEffect(Identifier abilityIdentifier) {
        return activeEffects.containsKey(abilityIdentifier);
    }

    public boolean hasAnyEffect() {
        return !activeEffects.isEmpty();
    }

    public boolean effectFinished(Identifier abilityIdentifier, Level level) {
        if (!hasEffect(abilityIdentifier)) return false;
        return level.getGameTime() > activeEffects.get(abilityIdentifier).expiryTime;
    }

    public void tickEffects(Level level, Player player) {
        Iterator<Map.Entry<Identifier, EffectEntry>> currentEffect = activeEffects.entrySet().iterator();
        while (currentEffect.hasNext()) {
            Map.Entry<Identifier, EffectEntry> entry = currentEffect.next();
            if (effectFinished(entry.getKey(), level)) {
                entry.getValue().onExpire().accept(player);
                currentEffect.remove();
            }
        }
    }
}