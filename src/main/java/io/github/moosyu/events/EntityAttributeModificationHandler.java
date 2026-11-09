package io.github.moosyu.events;

import io.github.moosyu.attributes.UnshatteredAttributeValue;
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
        event.add(EntityType.PLAYER, UnshatteredAttributeValue.MAX_HEALTH.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValue.DEFENCE.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValue.STRENGTH.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValue.CRITICAL_DAMAGE.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValue.CRITICAL_CHANCE.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValue.MANA.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValue.MANA_REGEN.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValue.HEALTH_REGEN.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValue.TRUE_DEFENCE.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValue.FEROCITY.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValue.DAMAGE.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValue.MINING_SPEED.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValue.MINING_FORTUNE.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValue.MINING_SPREAD.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValue.PRISTINE.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValue.FARMING_FORTUNE.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValue.FORAGING_FORTUNE.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValue.SWEEP.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValue.COMBAT_FORTUNE.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValue.FISHING_SPEED.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValue.FISHING_FORTUNE.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValue.FINAL_DAMAGE_MODIFIER.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValue.BREAKING_POWER.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValue.COMBAT_WISDOM.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValue.FARMING_WISDOM.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValue.FISHING_WISDOM.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValue.MINING_WISDOM.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValue.FORAGING_WISDOM.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValue.MAGECRAFT_WISDOM.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributeValue.CARPENTRY_WISDOM.holder);

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
        event.add(entityType, UnshatteredAttributeValue.CURRENT_HEALTH.holder, health);
        event.add(entityType, UnshatteredAttributeValue.MAX_HEALTH.holder, health);
    }

    public static void addHostileMob(EntityAttributeModificationEvent event, EntityType<? extends LivingEntity> entityType, double health, double damage) {
        event.add(entityType, UnshatteredAttributeValue.CURRENT_HEALTH.holder, health);
        event.add(entityType, UnshatteredAttributeValue.MAX_HEALTH.holder, health);
        event.add(entityType, UnshatteredAttributeValue.DAMAGE.holder, damage);
    }
}