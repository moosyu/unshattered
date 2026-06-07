package io.github.moosyu.events;

import io.github.moosyu.helpers.CheckSkillRequirementHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
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
    public static void onPlayerRightClick(PlayerInteractEvent.RightClickBlock event) {
        BlockPos pos = event.getPos();
        BlockState interactedBlock = event.getLevel().getBlockState(pos);
        // was thinking about a set or something for this but supposedly thatd be slower. i cant see how but whatever
        if (interactedBlock.is(Blocks.CRAFTING_TABLE)
                || interactedBlock.is(Blocks.FURNACE)
                || interactedBlock.is(Blocks.BLAST_FURNACE)
                || interactedBlock.is(BlockTags.COPPER_CHESTS)
                || interactedBlock.is(Blocks.ENDER_CHEST)
                || interactedBlock.is(BlockTags.SHULKER_BOXES)
                || interactedBlock.is(Blocks.BARREL)
                || interactedBlock.is(BlockTags.BEDS)
                || interactedBlock.is(BlockTags.ALL_SIGNS)
                || interactedBlock.is(Blocks.RESPAWN_ANCHOR)
                || interactedBlock.is(Blocks.LECTERN)
                || interactedBlock.is(BlockTags.WOODEN_SHELVES)
                || interactedBlock.is(Blocks.REDSTONE_TORCH)
                || interactedBlock.is(Blocks.SMOKER)
                || interactedBlock.is(Blocks.BLAST_FURNACE)
                || interactedBlock.is(Blocks.LOOM)
                || interactedBlock.is(Blocks.GRINDSTONE)
                || interactedBlock.is(Blocks.SMITHING_TABLE)
                || interactedBlock.is(Blocks.FLETCHING_TABLE)
                || interactedBlock.is(Blocks.CARTOGRAPHY_TABLE)
                || interactedBlock.is(Blocks.STONECUTTER)
                || interactedBlock.is(Blocks.BREWING_STAND)
                || interactedBlock.is(Blocks.ENCHANTING_TABLE)
        ) event.setCanceled(true);
        // anvils and crafting tables will have custom logic
        if (interactedBlock.is(Blocks.CRAFTING_TABLE)) {
            event.setCanceled(true);
            event.getEntity().swing(InteractionHand.MAIN_HAND);
        }
        if (interactedBlock.is(Blocks.ANVIL)) {
            event.setCanceled(true);
            event.getEntity().swing(InteractionHand.MAIN_HAND);
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