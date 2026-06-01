package io.github.moosyu.events;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class PlayerRightClickHandler {
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