package io.github.moosyu.data.regen.conditions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.moosyu.data.regen.RegenCondition;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public record NotBelowBlock(Block excludedBlock) implements RegenCondition {
    @Override
    public boolean test(Level level, BlockPos blockPos) {
        return level.getBlockState(blockPos.below()).getBlock() != excludedBlock;
    }

    @Override
    public MapCodec<? extends RegenCondition> codec() {
        return CODEC;
    }

    public static MapCodec<NotBelowBlock> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block_below_requirement").forGetter(NotBelowBlock::excludedBlock)
            ).apply(instance, NotBelowBlock::new)
    );
}
