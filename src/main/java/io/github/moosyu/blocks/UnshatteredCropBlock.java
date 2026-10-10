package io.github.moosyu.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;

public class UnshatteredCropBlock extends Block {
    public UnshatteredCropBlock(Properties properties) {
        super(properties.sound(SoundType.CROP)
                .noCollision()
                .noOcclusion()
        );
    }

    // seems to remove the shadow. not sure why noOcclusion doesn't though...
    @Override
    protected boolean propagatesSkylightDown(@NonNull BlockState state) {
        return state.getFluidState().isEmpty();
    }
}
