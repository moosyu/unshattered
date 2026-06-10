package io.github.moosyu.events;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;

import static io.github.moosyu.Unshattered.MODID;

// just in case some other mobs do something, player placement is handled in PlayerClickHandler
@EventBusSubscriber(modid = MODID)
public class BlockPlaceHandler {
    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (event.getLevel().isClientSide()) return;
        if (!(event.getEntity() instanceof Player player && player.isCreative())) event.setCanceled(true);
    }
}