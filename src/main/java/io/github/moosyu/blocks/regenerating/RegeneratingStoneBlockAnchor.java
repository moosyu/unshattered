package io.github.moosyu.blocks.regenerating;

import io.github.moosyu.blocks.RegeneratableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class RegeneratingStoneBlockAnchor extends Block implements RegeneratableBlock {
    public RegeneratingStoneBlockAnchor(Properties properties) {
        super(properties.destroyTime(1.5f).sound(SoundType.STONE).requiresCorrectToolForDrops());
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(regeneratingStage());
    }

    @Override
    public void tick(@NonNull BlockState state, @NonNull ServerLevel level, @NonNull BlockPos pos, @NonNull RandomSource random) {
        tickRegen(state, level, pos);
    }

        @Override
    public IntegerProperty regeneratingStage() {
        return IntegerProperty.create("regeneration_stage", 0, maxStage());
    }

    @Override
    public int maxStage() {
        return 3;
    }

    @Override
    public int regenDelayTicks() {
        return 0;
    }

    @Override
    public List<Block> blockPath() {
        return List.of();
    }
}
