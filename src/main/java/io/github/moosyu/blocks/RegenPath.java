package io.github.moosyu.blocks;

import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public record RegenPath(List<BlockState> path, int regenTicks) {}
