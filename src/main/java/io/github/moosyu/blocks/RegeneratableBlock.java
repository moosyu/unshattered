package io.github.moosyu.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import java.util.List;

public interface RegeneratableBlock {
    IntegerProperty regeneratingStage();
    int maxStage();
    int regenDelayTicks();
    List<Block> blockPath();

    default void tickRegen(BlockState state, ServerLevel level, BlockPos pos) {
        int stage = state.getValue(regeneratingStage());
        if (stage > 0) {
            level.setBlock(pos, state.setValue(regeneratingStage(), stage + 1), Block.UPDATE_CLIENTS);
            if (stage + 1 <= maxStage()) {
                level.scheduleTick(pos, (Block) this, regenDelayTicks());
            }
        }
    }

    default void damage(ServerLevel level, BlockPos pos, BlockState state, Identifier pathIdentifier) {
        Block self = (Block) this;
        int stage = state.getValue(regeneratingStage());
        if (stage < maxStage()) {
            level.setBlock(pos, state.setValue(regeneratingStage(), stage + 1), Block.UPDATE_CLIENTS);
            level.scheduleTick(pos, self, regenDelayTicks());
        }
    }
}
