package io.github.moosyu.events;

import io.github.moosyu.registers.AttributesRegistry;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;

import static io.github.moosyu.NNO.MODID;

public class EntityAttributeModificationHandler {
    @EventBusSubscriber(modid = MODID)
    public static class EventHandler {
        @SubscribeEvent
        public static void onModifyAttributesCalled(EntityAttributeModificationEvent event) {
            event.add(EntityType.PLAYER, AttributesRegistry.SWEEP);
            event.add(EntityType.PLAYER, AttributesRegistry.FORAGING_FORTUNE);
            event.add(EntityType.PLAYER, AttributesRegistry.HEALTH);
            event.add(EntityType.PLAYER, AttributesRegistry.DAMAGE);
            event.add(EntityType.PLAYER, AttributesRegistry.MANA);
            event.add(EntityType.PLAYER, AttributesRegistry.STRENGTH);
            event.add(EntityType.PLAYER, AttributesRegistry.CRITICAL_DAMAGE);
            event.add(EntityType.PLAYER, AttributesRegistry.CRITICAL_CHANCE);
            event.add(EntityType.PLAYER, AttributesRegistry.HEALTH_REGEN, 100.d);

            event.add(EntityType.ZOMBIE, AttributesRegistry.HEALTH, 70.0d);
            event.add(EntityType.ZOMBIE, AttributesRegistry.DAMAGE, 20.0d);

            event.add(EntityType.SKELETON, AttributesRegistry.HEALTH, 100.0d);
            event.add(EntityType.SKELETON, AttributesRegistry.DAMAGE, 15.0d);

            event.add(EntityType.SLIME, AttributesRegistry.HEALTH, 80.0d);
            event.add(EntityType.SLIME, AttributesRegistry.DAMAGE, 15.0d);

            event.add(EntityType.SPIDER, AttributesRegistry.HEALTH, 120.0d);
            event.add(EntityType.SPIDER, AttributesRegistry.DAMAGE, 35.0d);

            event.add(EntityType.CAVE_SPIDER, AttributesRegistry.HEALTH, 110.0d);
            event.add(EntityType.CAVE_SPIDER, AttributesRegistry.DAMAGE, 40.0d);

            event.add(EntityType.WITCH, AttributesRegistry.HEALTH, 150.0d);
            event.add(EntityType.WITCH, AttributesRegistry.DAMAGE, 20.0d);

            event.add(EntityType.ENDERMAN, AttributesRegistry.HEALTH, 160.0d);
            event.add(EntityType.ENDERMAN, AttributesRegistry.DAMAGE, 40.0d);

            event.add(EntityType.CREEPER, AttributesRegistry.HEALTH, 80.0d);
            event.add(EntityType.CREEPER, AttributesRegistry.DAMAGE, 20.0d);

            event.add(EntityType.SHEEP, AttributesRegistry.HEALTH, 50.0d);

            event.add(EntityType.COW, AttributesRegistry.HEALTH, 50.0d);

            event.add(EntityType.CHICKEN, AttributesRegistry.HEALTH, 20.0d);

            event.add(EntityType.RABBIT, AttributesRegistry.HEALTH, 130.0d);

            event.add(EntityType.PIG, AttributesRegistry.HEALTH, 50.0d);

            event.add(EntityType.MOOSHROOM, AttributesRegistry.HEALTH, 50.0d);
        }
    }
}
