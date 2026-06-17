package io.github.moosyu.helpers;

import io.github.moosyu.blocks.BrokenBlocksWorldResult;
import io.github.moosyu.datagen.UnshatteredBlockTagsProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

public final class CheckBreakableBlock {
    /**
     *
     * @param blockState the blockstate of the block being broken
     * @param player the player breaking the block
     * @return the blockstate of the block the broken block will be replaced with (or null if the player can't break the block)
     */
    public static BlockState canBreakBlock(BlockState blockState, Player player) {
        if (blockState.is(UnshatteredBlockTagsProvider.BREAKABLE_BLOCKS)) {
            return BrokenBlocksWorldResult.getDegradedState(blockState.getBlock());
        }
        return null;
    }
}

