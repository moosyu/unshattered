package io.github.moosyu.events;

import io.github.moosyu.helpers.CheckSkillRequirementHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class PlayerClickHandler {
    @SubscribeEvent
    public static void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getLevel().isClientSide()) return;
        BlockPos pos = event.getPos();
        BlockState interactedBlock = event.getLevel().getBlockState(pos);
        if (interactedBlock.is(Blocks.CRAFTING_TABLE)) {
            event.setCanceled(true);
            Player player = event.getEntity();
            player.swing(InteractionHand.MAIN_HAND);
        }
    }

    @SubscribeEvent
    public static void onPlayerRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (event.getLevel().isClientSide()) return;
        if (!CheckSkillRequirementHelper.canUseItem(event.getEntity(), event.getItemStack())) event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onPlayerLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        Level level = event.getLevel();
        if (level.isClientSide()) return;
        if (!CheckSkillRequirementHelper.canUseItem(event.getEntity(), event.getItemStack())) event.setCanceled(true);
    }
}