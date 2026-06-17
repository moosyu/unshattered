package io.github.moosyu.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public final class BrokenBlocksWorldResult {
    private static final Map<Block, BlockState> DEGRADATION_MAP = new HashMap<>();

    static {
        DEGRADATION_MAP.put(BlocksRegistry.BREAKABLE_STONE_BLOCK.get(), BlocksRegistry.BREAKABLE_COBBLESTONE_BLOCK.get().defaultBlockState());
        DEGRADATION_MAP.put(BlocksRegistry.BREAKABLE_COBBLESTONE_BLOCK.get(), Blocks.BEDROCK.defaultBlockState());
    }

    public static BlockState getDegradedState(Block block) {
        return DEGRADATION_MAP.getOrDefault(block, Blocks.AIR.defaultBlockState());
    }
}