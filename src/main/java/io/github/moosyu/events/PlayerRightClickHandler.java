package io.github.moosyu.events;

import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import static io.github.moosyu.Unshattered.MODID;

public class PlayerRightClickHandler {
    @EventBusSubscriber(modid = MODID)
    public static class EventHandler {
        @SubscribeEvent
        public static void onPlayerRightClick(PlayerInteractEvent.RightClickBlock event) {
            BlockPos pos = event.getPos();
            BlockState interactedBlock = event.getLevel().getBlockState(pos);
            if (interactedBlock.is(Blocks.CRAFTING_TABLE)) {
                event.setCanceled(true);
                Player player = event.getEntity();
                player.swing(InteractionHand.MAIN_HAND);
            }
        }
    }
}