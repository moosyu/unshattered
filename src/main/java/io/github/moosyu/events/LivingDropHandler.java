package io.github.moosyu.events;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class LivingDropHandler {
    @SubscribeEvent
    public static void onLivingDrop(LivingDropsEvent event) {
        if (event.getSource().getEntity() instanceof Player player && !player.level().isClientSide()) {
            event.setCanceled(true);
        }
    }
}