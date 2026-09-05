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
        event.add(EntityType.PLAYER, UnshatteredAttributeValues.DEFENCE.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValues.STRENGTH.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValues.CRITICAL_DAMAGE.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValues.CRITICAL_CHANCE.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValues.MANA.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValues.MANA_REGEN.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValues.HEALTH_REGEN.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValues.TRUE_DEFENCE.holder);
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
        event.add(EntityType.PLAYER, UnshatteredAttributeValues.BREAKING_POWER.holder);

        event.add(EntityType.ZOMBIE, UnshatteredAttributeValues.HEALTH.holder, 20.0);
        event.add(EntityType.ZOMBIE, UnshatteredAttributeValues.DAMAGE.holder, 5.0d);

        event.add(EntityType.SKELETON, UnshatteredAttributeValues.HEALTH.holder, 20.0d);
        event.add(EntityType.SKELETON, UnshatteredAttributeValues.DAMAGE.holder, 5.0);

        event.add(EntityType.SPIDER, UnshatteredAttributeValues.HEALTH.holder, 16.0d);
        event.add(EntityType.SPIDER, UnshatteredAttributeValues.DAMAGE.holder, 3.0d);

        event.add(EntityType.CAVE_SPIDER, UnshatteredAttributeValues.HEALTH.holder, 12.0d);
        event.add(EntityType.CAVE_SPIDER, UnshatteredAttributeValues.DAMAGE.holder, 4.0d);

        event.add(EntityType.WITCH, UnshatteredAttributeValues.HEALTH.holder, 26.0d);
        event.add(EntityType.WITCH, UnshatteredAttributeValues.DAMAGE.holder, 7.0d);

        event.add(EntityType.ENDERMAN, UnshatteredAttributeValues.HEALTH.holder, 40.0d);
        event.add(EntityType.ENDERMAN, UnshatteredAttributeValues.DAMAGE.holder, 8.5d);

        event.add(EntityType.CREEPER, UnshatteredAttributeValues.HEALTH.holder, 20.0d);
        event.add(EntityType.CREEPER, UnshatteredAttributeValues.DAMAGE.holder, 20.0d);

        event.add(EntityType.SQUID, UnshatteredAttributeValues.HEALTH.holder, 10.0d);

        event.add(EntityType.GLOW_SQUID, UnshatteredAttributeValues.HEALTH.holder, 40.0d);

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

        event.add(EntityType.MOOSHROOM, UnshatteredAttributeValues.HEALTH.holder, 10.0d);

        event.add(EntityType.SHEEP, UnshatteredAttributeValues.HEALTH.holder, 8.0d);

        event.add(EntityType.COW, UnshatteredAttributeValues.HEALTH.holder, 10.0d);

        event.add(EntityType.CHICKEN, UnshatteredAttributeValues.HEALTH.holder, 4.0d);

        event.add(EntityType.RABBIT, UnshatteredAttributeValues.HEALTH.holder, 10.0d);

        event.add(EntityType.PIG, UnshatteredAttributeValues.HEALTH.holder, 10.0d);

        event.add(EntityType.ARMOR_STAND, UnshatteredAttributeValues.HEALTH.holder, 0.1d);

        event.add(EntityType.ENDERMITE, UnshatteredAttributeValues.HEALTH.holder, 50.0d);
        event.add(EntityType.ENDERMITE, UnshatteredAttributeValues.DAMAGE.holder, 7.0);
    }
}
