package io.github.moosyu.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NonNull;

public class UnshatteredMushroomBlock extends Block {
    public UnshatteredMushroomBlock(Properties properties) {
        super(properties.sound(SoundType.GRASS)
                .noOcclusion()
                .noCollision()
        );
    }

    @Override
    protected @NonNull VoxelShape getShape(@NonNull BlockState state, @NonNull BlockGetter level, @NonNull BlockPos pos, @NonNull CollisionContext context) {
        return Block.column(6.0f, 0.0f, 6.0f);
    }
}
