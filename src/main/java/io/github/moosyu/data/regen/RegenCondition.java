package io.github.moosyu.data.regen;

import com.mojang.serialization.Codec;
import io.github.moosyu.data.fishing.FishingCondition;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Blocks;
import org.jspecify.annotations.NonNull;

import java.util.Locale;
import java.util.function.BiPredicate;

public enum RegenCondition implements StringRepresentable {
    NOT_ABOVE_AIR((level, blockPos) -> level.getBlockState(blockPos.below()).getBlock() != Blocks.AIR);

    private final BiPredicate<ServerLevel, BlockPos> condition;

    RegenCondition(BiPredicate<ServerLevel, BlockPos> condition) {
        this.condition = condition;
    }

    public static Codec<RegenCondition> CODEC = StringRepresentable.fromEnum(RegenCondition::values);

    public boolean test(ServerLevel level, BlockPos blockPos) {
        return condition.test(level, blockPos);
    }

    @Override
    public @NonNull String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
