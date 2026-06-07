package io.github.moosyu.blocks;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public final class BrokenBlocksWorldResult {
    // its kind of crazy that i basically work for google now
    private static final BiMap<Block, Block> BROKEN_BLOCKS_WORLD_RESULT = ImmutableBiMap.ofEntries(
            Map.entry(BlocksRegistry.BREAKABLE_STONE_BLOCK.get(), BlocksRegistry.BREAKABLE_COBBLESTONE_BLOCK.get()),
            Map.entry(BlocksRegistry.BREAKABLE_COBBLESTONE_BLOCK.get(), Blocks.BEDROCK)
    );

    public static BlockState getBlockCreated(Block block) {
        return BROKEN_BLOCKS_WORLD_RESULT.getOrDefault(block, Blocks.AIR).defaultBlockState();
    }

    public static BlockState getBlockRestored(Block block) {
        return BROKEN_BLOCKS_WORLD_RESULT.inverse().getOrDefault(block, Blocks.AIR).defaultBlockState();
    }
}
