package io.github.moosyu.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public final class BrokenBlocksWorldResult {
    private static final Map<Block, Block> BROKEN_BLOCKS_WORLD_RESULT = Map.ofEntries(
            Map.entry(Blocks.STONE, Blocks.COBBLESTONE),
            Map.entry(Blocks.COBBLESTONE, Blocks.BEDROCK)
    );

    public static BlockState getBlockCreated(Block block) {
        return BROKEN_BLOCKS_WORLD_RESULT.getOrDefault(block, Blocks.AIR).defaultBlockState();
    }
}
