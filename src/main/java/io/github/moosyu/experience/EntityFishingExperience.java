package io.github.moosyu.experience;

import net.minecraft.world.entity.EntityType;
import java.util.Map;

public class EntityFishingExperience {
    private static final Map<EntityType<?>, Float> ENTITY_FISHING_EXP = Map.ofEntries(
            Map.entry(EntityType.SQUID, 25.0f),
            Map.entry(EntityType.GLOW_SQUID, 90.0f)
    );

    public static float getExp(EntityType<?> entityType) {
        return ENTITY_FISHING_EXP.getOrDefault(entityType, 0.0f);
    }
}
