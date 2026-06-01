package io.github.moosyu.events;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;


import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class LivingEntityUseItemHandler {
    @SubscribeEvent
    public static void onLivingEntityUseItem(LivingEntityUseItemEvent.Start event) {
        if (event.getEntity() instanceof Player player && !player.level().isClientSide() && event.getItem().has(DataComponents.FOOD)) {
            event.setCanceled(true);
        }
    }
}
