package io.github.moosyu.experience;

import net.minecraft.world.entity.EntityType;
import java.util.Map;

public class EntityCombatExperience {
    private static final Map<EntityType<?>, Float> COMBAT_EXP = Map.ofEntries(
            Map.entry(EntityType.ZOMBIE, 6.0f),
            Map.entry(EntityType.SKELETON, 6.0f),
            Map.entry(EntityType.SLIME, 6.0f),
            Map.entry(EntityType.SPIDER, 6.0f),
            Map.entry(EntityType.CAVE_SPIDER, 6.0f),
            Map.entry(EntityType.WITCH, 6.0f),
            Map.entry(EntityType.ENDERMAN, 6.0f),
            Map.entry(EntityType.BAT, 6.0f),
            Map.entry(EntityType.CREEPER, 6.0f),
            Map.entry(EntityType.GHAST, 6.0f),
            Map.entry(EntityType.BLAZE, 6.0f)
    );

    public static float getExp(EntityType<?> entityType) {
        return COMBAT_EXP.getOrDefault(entityType, 0.0f);
    }
}
