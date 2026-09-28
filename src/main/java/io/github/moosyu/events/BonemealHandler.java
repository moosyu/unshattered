package io.github.moosyu.events;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.BonemealEvent;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class BonemealHandler {
    @SubscribeEvent
    public static void onPlayerBonemeal(BonemealEvent event) {
        event.setCanceled(true);
    }
}
