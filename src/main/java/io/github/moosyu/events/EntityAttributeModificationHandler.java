package io.github.moosyu.events;

import io.github.moosyu.attributes.UnshatteredAttributeValues;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class EntityAttributeModificationHandler {
    @SubscribeEvent
    public static void onModifyAttributesCalled(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, UnshatteredAttributeValues.MAX_HEALTH.holder);
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
        event.add(EntityType.PLAYER, UnshatteredAttributeValues.COMBAT_WISDOM.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValues.FARMING_WISDOM.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValues.FISHING_WISDOM.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValues.MINING_WISDOM.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValues.FORAGING_WISDOM.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValues.MAGECRAFT_WISDOM.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValues.CARPENTRY_WISDOM.holder);

        addHostileMob(event, EntityType.ZOMBIE, 20.0d, 5.0d);

        addHostileMob(event, EntityType.SKELETON, 20.0d, 5.0d);

        addHostileMob(event, EntityType.SPIDER, 16.0d, 3.0d);

        addHostileMob(event, EntityType.CAVE_SPIDER, 12.0d, 4.0d);

        addHostileMob(event, EntityType.WITCH, 26.0d, 7.0d);

        addHostileMob(event, EntityType.ENDERMAN, 40.0d, 8.5d);

        addHostileMob(event, EntityType.CREEPER, 20.0d, 20.0d);

        addPassiveMob(event, EntityType.SQUID, 10.0d);

        addPassiveMob(event, EntityType.GLOW_SQUID, 40.0d);

        addPassiveMob(event, EntityType.MOOSHROOM, 10.0d);

        addPassiveMob(event, EntityType.SHEEP, 8.0d);

        addPassiveMob(event, EntityType.COW, 10.0d);

        addPassiveMob(event, EntityType.CHICKEN, 4.0d);

        addPassiveMob(event, EntityType.RABBIT, 10.0d);

        addPassiveMob(event, EntityType.PIG, 10.0d);

        addPassiveMob(event, EntityType.ARMOR_STAND, 0.1d);

        addHostileMob(event, EntityType.ENDERMITE, 50.0d, 7.0d);

        // the actual values will be set based on size on level join
        addHostileMob(event, EntityType.SLIME, 0.0d, 0.0d);

        addHostileMob(event, EntityType.ZOMBIE_VILLAGER, 25.0d, 7.0d);
    }

    public static void addPassiveMob(EntityAttributeModificationEvent event, EntityType<? extends LivingEntity> entityType, double health) {
        event.add(entityType, UnshatteredAttributeValues.CURRENT_HEALTH.holder, health);
        event.add(entityType, UnshatteredAttributeValues.MAX_HEALTH.holder, health);
    }

    public static void addHostileMob(EntityAttributeModificationEvent event, EntityType<? extends LivingEntity> entityType, double health, double damage) {
        event.add(entityType, UnshatteredAttributeValues.CURRENT_HEALTH.holder, health);
        event.add(entityType, UnshatteredAttributeValues.MAX_HEALTH.holder, health);
        event.add(entityType, UnshatteredAttributeValues.DAMAGE.holder, damage);
    }
}