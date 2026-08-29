package io.github.moosyu.data.regen;

import io.github.moosyu.blocks.UnshatteredBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RegenPaths {
    public record RegenPath(List<BlockState> path, int regenTicks, int stagesIncremented) {
        public RegenPath(List<BlockState> path, int regenTicks) {
            this(path, regenTicks, 1);
        }
    }

    public static final Map<String, RegenPath> REGEN_STAGES = Map.ofEntries(
            createRegenPath("stone", List.of(UnshatteredBlocks.BREAKABLE_STONE_BLOCK.get(),
                    UnshatteredBlocks.BREAKABLE_COBBLESTONE_BLOCK.get(),
                    Blocks.BEDROCK), 120),
            createRegenPath("coal", List.of(UnshatteredBlocks.BREAKABLE_COAL_ORE_BLOCK.get(),
                    UnshatteredBlocks.BREAKABLE_COBBLESTONE_BLOCK.get(), Blocks.BEDROCK), 150),
            Map.entry("wheat", new RegenPath(List.of(UnshatteredBlocks.BREAKABLE_WHEAT_BLOCK.get().defaultBlockState(),
                    Blocks.WHEAT.defaultBlockState().setValue(CropBlock.AGE, 6),
                    Blocks.WHEAT.defaultBlockState().setValue(CropBlock.AGE, 5),
                    Blocks.WHEAT.defaultBlockState().setValue(CropBlock.AGE, 4),
                    Blocks.WHEAT.defaultBlockState().setValue(CropBlock.AGE, 3),
                    Blocks.WHEAT.defaultBlockState().setValue(CropBlock.AGE, 2),
                    Blocks.WHEAT.defaultBlockState().setValue(CropBlock.AGE, 1),
                    Blocks.WHEAT.defaultBlockState().setValue(CropBlock.AGE, 0)), 200, 6)
            )
    );

    public static final Map<BlockState, String> REGEN_IDENTIFIER_BY_BLOCK = REGEN_STAGES.entrySet().stream()
            .collect(Collectors.toUnmodifiableMap(
                    entry -> entry.getValue().path().getFirst(), Map.Entry::getKey)
            );

    private static Map.Entry<String, RegenPath> createRegenPath(String identifier, List<Block> blocks, int regenTicks) {
        return Map.entry(identifier, new RegenPath(blocks.stream().map(Block::defaultBlockState).toList(), regenTicks));
    }
}
