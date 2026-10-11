package io.github.moosyu.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NonNull;

import javax.annotation.Nullable;
import java.util.Map;

public class UnshatteredCocoaBeanBlock extends HorizontalDirectionalBlock {
    public static final MapCodec<UnshatteredCocoaBeanBlock> CODEC = simpleCodec(UnshatteredCocoaBeanBlock::new);

    private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateHorizontal(
            Block.column(8.0, 3.0, 12.0)
                    .move(0.0, 0.0, (2 - 5) / 16.0)
                    .optimize()
    );

    public UnshatteredCocoaBeanBlock(Properties properties) {
        super(properties.sound(SoundType.WOOD)
                .destroyTime(0.2f)
                .noOcclusion()
        );
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public @NonNull MapCodec<UnshatteredCocoaBeanBlock> codec() {
        return CODEC;
    }

    @Override
    protected @NonNull VoxelShape getShape(BlockState state, @NonNull BlockGetter level, @NonNull BlockPos pos, @NonNull CollisionContext context) {
        return SHAPES.get(state.getValue(FACING));
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        for (Direction direction : context.getNearestLookingDirections()) {
            if (direction.getAxis().isHorizontal()) {
                return this.defaultBlockState().setValue(FACING, direction);
            }
        }
        return null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}