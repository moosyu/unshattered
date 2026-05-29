package io.github.moosyu.events;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;

import static io.github.moosyu.Unshattered.MODID;

public class LivingExperienceDropHandler {
    @EventBusSubscriber(modid = MODID)
    public static class EventHandler {
        @SubscribeEvent
        public static void onLivingExperienceDrop(LivingExperienceDropEvent event) {
            if (!event.getEntity().level().isClientSide()) event.setCanceled(true);
        }
    }
}
