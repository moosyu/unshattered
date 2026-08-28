package io.github.moosyu.data.regen;

import io.github.moosyu.blocks.RegenPath;
import io.github.moosyu.blocks.UnshatteredBlocks;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RegenPaths {
    public static final Map<String, RegenPath> REGEN_STAGES = Map.ofEntries(
            Map.entry("stone",
                    new RegenPath(List.of(UnshatteredBlocks.BREAKABLE_STONE_BLOCK.get().defaultBlockState(),
                            UnshatteredBlocks.BREAKABLE_COBBLESTONE_BLOCK.get().defaultBlockState(),
                            Blocks.BEDROCK.defaultBlockState()),
                            150
                    )
            )
    );

    public static final Map<BlockState, String> REGEN_IDENTIFIER_BY_BLOCK = REGEN_STAGES.entrySet().stream()
            .collect(Collectors.toUnmodifiableMap(
                    entry -> entry.getValue().path().getFirst(), Map.Entry::getKey)
            );
}
