package io.github.moosyu.events;

import io.github.moosyu.attributes.UnshatteredAttributes;
import io.github.moosyu.entities.EntitiesRegistry;
import net.minecraft.world.attribute.AttributeTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class EntityAttributeModificationHandler {
    @SubscribeEvent
    public static void onModifyAttributesCalled(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, UnshatteredAttributes.HEALTH.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributes.DEFENSE.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributes.STRENGTH.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributes.CRITICAL_DAMAGE.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributes.CRITICAL_CHANCE.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributes.MANA.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributes.HEALTH_REGEN.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributes.TRUE_DEFENSE.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributes.FEROCITY.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributes.DAMAGE.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributes.MINING_SPEED.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributes.MINING_FORTUNE.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributes.MINING_SPREAD.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributes.PRISTINE.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributes.FARMING_FORTUNE.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributes.FORAGING_FORTUNE.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributes.SWEEP.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributes.MAGIC_FIND.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributes.FINAL_DAMAGE_MODIFIER.holder);

        event.add(EntityType.ZOMBIE, UnshatteredAttributes.HEALTH.holder, 70.0d);
        event.add(EntityType.ZOMBIE, UnshatteredAttributes.DAMAGE.holder, 20.0d);

        event.add(EntityType.SKELETON, UnshatteredAttributes.HEALTH.holder, 100.0d);
        event.add(EntityType.SKELETON, UnshatteredAttributes.DAMAGE.holder, 15.0d);

        event.add(EntityType.SLIME, UnshatteredAttributes.HEALTH.holder, 80.0d);
        event.add(EntityType.SLIME, UnshatteredAttributes.DAMAGE.holder, 15.0d);

        event.add(EntityType.SPIDER, UnshatteredAttributes.HEALTH.holder, 120.0d);
        event.add(EntityType.SPIDER, UnshatteredAttributes.DAMAGE.holder, 35.0d);

        event.add(EntityType.CAVE_SPIDER, UnshatteredAttributes.HEALTH.holder, 110.0d);
        event.add(EntityType.CAVE_SPIDER, UnshatteredAttributes.DAMAGE.holder, 40.0d);

        event.add(EntityType.WITCH, UnshatteredAttributes.HEALTH.holder, 150.0d);
        event.add(EntityType.WITCH, UnshatteredAttributes.DAMAGE.holder, 20.0d);

        event.add(EntityType.ENDERMAN, UnshatteredAttributes.HEALTH.holder, 160.0d);
        event.add(EntityType.ENDERMAN, UnshatteredAttributes.DAMAGE.holder, 40.0d);

        event.add(EntityType.CREEPER, UnshatteredAttributes.HEALTH.holder, 80.0d);
        event.add(EntityType.CREEPER, UnshatteredAttributes.DAMAGE.holder, 20.0d);

        event.add(EntitiesRegistry.GRAVEYARD_ZOMBIE_VILLAGER.get(), UnshatteredAttributes.HEALTH.holder, 80.0d);
        event.add(EntitiesRegistry.GRAVEYARD_ZOMBIE_VILLAGER.get(), UnshatteredAttributes.DAMAGE.holder, 20.0d);
        event.add(EntitiesRegistry.GRAVEYARD_ZOMBIE_VILLAGER.get(), Attributes.MAX_HEALTH, 1.0d);
        event.add(EntitiesRegistry.GRAVEYARD_ZOMBIE_VILLAGER.get(), Attributes.FOLLOW_RANGE, 1.0d);
        event.add(EntitiesRegistry.GRAVEYARD_ZOMBIE_VILLAGER.get(), Attributes.WAYPOINT_TRANSMIT_RANGE, 1.0d);
        event.add(EntitiesRegistry.GRAVEYARD_ZOMBIE_VILLAGER.get(), Attributes.WAYPOINT_RECEIVE_RANGE, 1.0d);
        event.add(EntitiesRegistry.GRAVEYARD_ZOMBIE_VILLAGER.get(), Attributes.STEP_HEIGHT, 0.0d);
        event.add(EntitiesRegistry.GRAVEYARD_ZOMBIE_VILLAGER.get(), Attributes.MOVEMENT_EFFICIENCY, 0.0d);
        event.add(EntitiesRegistry.GRAVEYARD_ZOMBIE_VILLAGER.get(), Attributes.MOVEMENT_SPEED, 0.7d);
        event.add(EntitiesRegistry.GRAVEYARD_ZOMBIE_VILLAGER.get(), Attributes.SCALE, 1.0d);
        event.add(EntitiesRegistry.GRAVEYARD_ZOMBIE_VILLAGER.get(), Attributes.GRAVITY, 0.08d);
        event.add(EntitiesRegistry.GRAVEYARD_ZOMBIE_VILLAGER.get(), Attributes.ARMOR, 0.0d);
        event.add(EntitiesRegistry.GRAVEYARD_ZOMBIE_VILLAGER.get(), Attributes.ARMOR_TOUGHNESS, 0.0d);
        event.add(EntitiesRegistry.GRAVEYARD_ZOMBIE_VILLAGER.get(), Attributes.MAX_ABSORPTION, 0.0d);
        event.add(EntitiesRegistry.GRAVEYARD_ZOMBIE_VILLAGER.get(), Attributes.KNOCKBACK_RESISTANCE, 0.0d);
        event.add(EntitiesRegistry.GRAVEYARD_ZOMBIE_VILLAGER.get(), Attributes.SAFE_FALL_DISTANCE, 1024.0d);
        event.add(EntitiesRegistry.GRAVEYARD_ZOMBIE_VILLAGER.get(), Attributes.FALL_DAMAGE_MULTIPLIER, 0.0d);

        event.add(EntityType.MOOSHROOM, UnshatteredAttributes.HEALTH.holder, 50.0d);

        event.add(EntityType.SHEEP, UnshatteredAttributes.HEALTH.holder, 50.0d);

        event.add(EntityType.COW, UnshatteredAttributes.HEALTH.holder, 50.0d);

        event.add(EntityType.CHICKEN, UnshatteredAttributes.HEALTH.holder, 20.0d);

        event.add(EntityType.RABBIT, UnshatteredAttributes.HEALTH.holder, 130.0d);

        event.add(EntityType.PIG, UnshatteredAttributes.HEALTH.holder, 50.0d);

        event.add(EntityType.MOOSHROOM, UnshatteredAttributes.HEALTH.holder, 50.0d);
    }
}
