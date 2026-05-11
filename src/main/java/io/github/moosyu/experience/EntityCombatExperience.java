package io.github.moosyu.experience;

import net.minecraft.world.entity.EntityType;
import java.util.Map;

public class EntityCombatExperience {
    private static final Map<EntityType<?>, Float> ENTITY_COMBAT_EXP = Map.ofEntries(
            Map.entry(EntityType.ZOMBIE, 6.0f),
            Map.entry(EntityType.SKELETON, 6.0f),
            Map.entry(EntityType.SLIME, 4.0f),
            Map.entry(EntityType.SPIDER, 8.0f),
            Map.entry(EntityType.CAVE_SPIDER, 8.0f),
            Map.entry(EntityType.WITCH, 15.0f),
            Map.entry(EntityType.ENDERMAN, 15.0f),
            Map.entry(EntityType.BAT, 33.0f),
            Map.entry(EntityType.CREEPER, 8.0f),
            Map.entry(EntityType.BLAZE, 10.0f)
    );

    public static float getExp(EntityType<?> entityType) {
        return ENTITY_COMBAT_EXP.getOrDefault(entityType, 0.0f);
    }
}
