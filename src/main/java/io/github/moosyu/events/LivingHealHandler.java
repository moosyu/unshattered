package io.github.moosyu.events;

import io.github.moosyu.helpers.HealingManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;

import static io.github.moosyu.NNO.MODID;

// todo: make sure there isn't a chance this + vanilla healing occurs
public class LivingHealHandler {
    @EventBusSubscriber(modid = MODID)
    public static class EventHandler {
        @SubscribeEvent
        public static void onLivingHeal(LivingHealEvent event) {
            if (!HealingManager.isAllowed()) event.setCanceled(true);
        }
    }
}