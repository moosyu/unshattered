package io.github.moosyu.util;

import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.blocks.BrokenBlocksWorldResult;
import io.github.moosyu.data.UnshatteredDataMaps;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.datagen.UnshatteredBlockTagsProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;

public final class BlockBreakingUtil {
    /**
     * @param blockState the blockstate of the block being broken
     * @param player the player breaking the block
     * @return the blockstate of the block the broken block will be replaced with (or null if the player can't break the block)
     */
    public static BlockState isBreakableBlock(BlockState blockState, Player player) {
        if (blockState.is(UnshatteredBlockTagsProvider.BREAKABLE_BLOCKS)) {
            return BrokenBlocksWorldResult.getDegradedState(blockState.getBlock());
        }
        return null;
    }

    /**
     * check if a player can break a given block based on their breaking power, sending a message if not
     * @param player player breaking the block
     * @param block block attempting to be broken
     * @return whether the block can be broken by the player
     */
    public static boolean canBreakBlock(Player player, Holder<Block> block) {
        int requiredBreakingPower = Objects.requireNonNullElse(block.getData(UnshatteredDataMaps.BLOCK_BREAKING_POWER_DATA), 0);
        int playerBreakingPower = (int) player.getAttributeValue(UnshatteredAttributeValues.BREAKING_POWER.holder);
        if (playerBreakingPower >= requiredBreakingPower) return true;
        else if (!player.getData(UnshatteredAttachments.PLAYER_FAILED_REQUIREMENT_MESSAGED_FIRED) && !player.level().isClientSide()) {
            player.sendSystemMessage(
                    Component.translatable(player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty() ? "breaking_power.messages.unshattered.hand" : "breaking_power.messages.unshattered.tool")
                            .append(Component.translatable("breaking_power.messages.unshattered.generic_breaking_power_requirment_start"))
                            .append(Component.literal(requiredBreakingPower + UnshatteredAttributeValues.BREAKING_POWER.symbol).withColor(UnshatteredAttributeValues.BREAKING_POWER.color))
                            .append(Component.translatable("breaking_power.messages.unshattered.generic_breaking_power_requirment_end"))
                            .append(".")
            );
            player.setData(UnshatteredAttachments.PLAYER_FAILED_REQUIREMENT_MESSAGED_FIRED, true);
        }
        return false;
    }
}

