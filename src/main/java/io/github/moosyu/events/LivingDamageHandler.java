package io.github.moosyu.events;

import net.minecraft.tags.DamageTypeTags;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import static io.github.moosyu.NNO.MODID;

// allows damage being dealt while mob on fire
public class LivingDamageHandler {
    @EventBusSubscriber(modid = MODID)
    public static class EventHandler {
        @SubscribeEvent
        public static void onLivingDamage(LivingDamageEvent.Pre event) {
            if (event.getSource().is(DamageTypeTags.IS_FIRE)) {
                event.getEntity().invulnerableTime = 0;
            }
        }
    }
}
