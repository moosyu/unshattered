package io.github.moosyu.events;

import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.entities.UnshatteredEntities;
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
        event.add(EntityType.PLAYER, UnshatteredAttributeValues.HEALTH.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValues.DEFENSE.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValues.STRENGTH.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValues.CRITICAL_DAMAGE.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValues.CRITICAL_CHANCE.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValues.MANA.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValues.MANA_REGEN.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValues.HEALTH_REGEN.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValues.TRUE_DEFENSE.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValues.FEROCITY.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValues.DAMAGE.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValues.MINING_SPEED.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValues.MINING_FORTUNE.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValues.MINING_SPREAD.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValues.PRISTINE.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValues.FARMING_FORTUNE.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValues.FORAGING_FORTUNE.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValues.SWEEP.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValues.COMBAT_FORTUNE.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValues.FISHING_SPEED.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValues.FISHING_FORTUNE.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValues.FINAL_DAMAGE_MODIFIER.holder);

        event.add(EntityType.ZOMBIE, UnshatteredAttributeValues.HEALTH.holder, 70.0d);
        event.add(EntityType.ZOMBIE, UnshatteredAttributeValues.DAMAGE.holder, 20.0d);

        event.add(EntityType.SKELETON, UnshatteredAttributeValues.HEALTH.holder, 100.0d);
        event.add(EntityType.SKELETON, UnshatteredAttributeValues.DAMAGE.holder, 15.0d);

        event.add(EntityType.SLIME, UnshatteredAttributeValues.HEALTH.holder, 80.0d);
        event.add(EntityType.SLIME, UnshatteredAttributeValues.DAMAGE.holder, 15.0d);

        event.add(EntityType.SPIDER, UnshatteredAttributeValues.HEALTH.holder, 120.0d);
        event.add(EntityType.SPIDER, UnshatteredAttributeValues.DAMAGE.holder, 35.0d);

        event.add(EntityType.CAVE_SPIDER, UnshatteredAttributeValues.HEALTH.holder, 110.0d);
        event.add(EntityType.CAVE_SPIDER, UnshatteredAttributeValues.DAMAGE.holder, 40.0d);

        event.add(EntityType.WITCH, UnshatteredAttributeValues.HEALTH.holder, 150.0d);
        event.add(EntityType.WITCH, UnshatteredAttributeValues.DAMAGE.holder, 20.0d);

        event.add(EntityType.ENDERMAN, UnshatteredAttributeValues.HEALTH.holder, 160.0d);
        event.add(EntityType.ENDERMAN, UnshatteredAttributeValues.DAMAGE.holder, 40.0d);

        event.add(EntityType.CREEPER, UnshatteredAttributeValues.HEALTH.holder, 80.0d);
        event.add(EntityType.CREEPER, UnshatteredAttributeValues.DAMAGE.holder, 20.0d);

        event.add(UnshatteredEntities.GRAVEYARD_ZOMBIE_VILLAGER.get(), UnshatteredAttributeValues.HEALTH.holder, 80.0d);
        event.add(UnshatteredEntities.GRAVEYARD_ZOMBIE_VILLAGER.get(), UnshatteredAttributeValues.DAMAGE.holder, 20.0d);
        event.add(UnshatteredEntities.GRAVEYARD_ZOMBIE_VILLAGER.get(), Attributes.MAX_HEALTH, 1.0d);
        event.add(UnshatteredEntities.GRAVEYARD_ZOMBIE_VILLAGER.get(), Attributes.FOLLOW_RANGE, 1.0d);
        event.add(UnshatteredEntities.GRAVEYARD_ZOMBIE_VILLAGER.get(), Attributes.WAYPOINT_TRANSMIT_RANGE, 1.0d);
        event.add(UnshatteredEntities.GRAVEYARD_ZOMBIE_VILLAGER.get(), Attributes.WAYPOINT_RECEIVE_RANGE, 1.0d);
        event.add(UnshatteredEntities.GRAVEYARD_ZOMBIE_VILLAGER.get(), Attributes.STEP_HEIGHT, 0.0d);
        event.add(UnshatteredEntities.GRAVEYARD_ZOMBIE_VILLAGER.get(), Attributes.MOVEMENT_EFFICIENCY, 0.0d);
        event.add(UnshatteredEntities.GRAVEYARD_ZOMBIE_VILLAGER.get(), Attributes.MOVEMENT_SPEED, 0.7d);
        event.add(UnshatteredEntities.GRAVEYARD_ZOMBIE_VILLAGER.get(), Attributes.SCALE, 1.0d);
        event.add(UnshatteredEntities.GRAVEYARD_ZOMBIE_VILLAGER.get(), Attributes.GRAVITY, 0.08d);
        event.add(UnshatteredEntities.GRAVEYARD_ZOMBIE_VILLAGER.get(), Attributes.ARMOR, 0.0d);
        event.add(UnshatteredEntities.GRAVEYARD_ZOMBIE_VILLAGER.get(), Attributes.ARMOR_TOUGHNESS, 0.0d);
        event.add(UnshatteredEntities.GRAVEYARD_ZOMBIE_VILLAGER.get(), Attributes.MAX_ABSORPTION, 0.0d);
        event.add(UnshatteredEntities.GRAVEYARD_ZOMBIE_VILLAGER.get(), Attributes.KNOCKBACK_RESISTANCE, 0.0d);
        event.add(UnshatteredEntities.GRAVEYARD_ZOMBIE_VILLAGER.get(), Attributes.SAFE_FALL_DISTANCE, 1024.0d);
        event.add(UnshatteredEntities.GRAVEYARD_ZOMBIE_VILLAGER.get(), Attributes.FALL_DAMAGE_MULTIPLIER, 0.0d);

        event.add(EntityType.MOOSHROOM, UnshatteredAttributeValues.HEALTH.holder, 50.0d);

        event.add(EntityType.SHEEP, UnshatteredAttributeValues.HEALTH.holder, 50.0d);

        event.add(EntityType.COW, UnshatteredAttributeValues.HEALTH.holder, 50.0d);

        event.add(EntityType.CHICKEN, UnshatteredAttributeValues.HEALTH.holder, 20.0d);

        event.add(EntityType.RABBIT, UnshatteredAttributeValues.HEALTH.holder, 130.0d);

        event.add(EntityType.PIG, UnshatteredAttributeValues.HEALTH.holder, 50.0d);

        event.add(EntityType.MOOSHROOM, UnshatteredAttributeValues.HEALTH.holder, 50.0d);
    }
}
