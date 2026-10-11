package io.github.moosyu.data.regen;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface RegenCondition {
    boolean test(Level level, BlockPos blockPos);

    MapCodec<? extends RegenCondition> codec();
}
