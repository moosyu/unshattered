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
        }
    }
}
