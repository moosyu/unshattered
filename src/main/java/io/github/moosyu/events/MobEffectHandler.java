package io.github.moosyu.events;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;

import static io.github.moosyu.Unshattered.MODID;

// disables potion effects for players and mobs
@EventBusSubscriber(modid = MODID)
public class MobEffectHandler {
    @SubscribeEvent
    public static void onMobEffect(MobEffectEvent.Applicable event) {
        if (event.getEntity().level().isClientSide()) return;
        event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
    }
}
