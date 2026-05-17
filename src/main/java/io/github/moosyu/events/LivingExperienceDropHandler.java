package io.github.moosyu.events;

import io.github.moosyu.helpers.HealingManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;

import static io.github.moosyu.NNO.MODID;

public class LivingExperienceDropHandler {
    @EventBusSubscriber(modid = MODID)
    public static class EventHandler {
        @SubscribeEvent
        public static void onLivingExperienceDrop(LivingExperienceDropEvent event) {
            event.setCanceled(true);
        }
    }
}
