package io.github.moosyu.skills.experience;

import net.minecraft.world.entity.EntityType;
import java.util.Map;

public class EntityFarmingExperience {
    private static final Map<EntityType<?>, Float> ENTITY_FARMING_EXP = Map.ofEntries(
            Map.entry(EntityType.SHEEP, 4.0f),
            Map.entry(EntityType.COW, 3.0f),
            Map.entry(EntityType.CHICKEN, 4.0f),
            Map.entry(EntityType.RABBIT, 4.0f),
            Map.entry(EntityType.PIG, 4.0f),
            Map.entry(EntityType.MOOSHROOM, 5.0f)
    );

    public static float getExp(EntityType<?> entityType) {
        return ENTITY_FARMING_EXP.getOrDefault(entityType, 0.0f);
    }
}
