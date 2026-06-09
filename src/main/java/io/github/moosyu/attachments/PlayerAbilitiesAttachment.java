package io.github.moosyu.attachments;

import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public class PlayerAbilitiesAttachment {
    private final Map<Identifier, Long> activeEffects = new HashMap<>();

    public void addEffect(Identifier abilityIdentifier, long abilityLength, Level level) {
        activeEffects.put(abilityIdentifier, level.getGameTime() + abilityLength);
    }

    public void setEffectExpiryTime(Identifier abilityIdentifier, long abilityLength, Level level) {
        activeEffects.replace(abilityIdentifier, level.getGameTime() + abilityLength);
    }

    public void removeEffect(Identifier abilityIdentifier) {
        activeEffects.remove(abilityIdentifier);
    }

    public boolean hasEffect(Identifier abilityIdentifier) {
        return activeEffects.containsKey(abilityIdentifier);
    }

    public boolean effectFinished(Identifier abilityIdentifier, Level level) {
        if (!hasEffect(abilityIdentifier)) return false;
        return level.getGameTime() > activeEffects.get(abilityIdentifier);
    }
}