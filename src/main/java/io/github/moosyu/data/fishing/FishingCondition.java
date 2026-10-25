package io.github.moosyu.data.fishing;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.material.Fluids;
import org.jspecify.annotations.NonNull;

import java.util.Locale;
import java.util.function.BiPredicate;

public enum FishingCondition implements StringRepresentable {
    NIGHT((level, _) -> level.isDarkOutside()),
    WATER((level, blockPos) -> level.getFluidState(blockPos).is(Fluids.WATER));

    private final BiPredicate<ServerLevel, BlockPos> condition;
    FishingCondition(BiPredicate<ServerLevel, BlockPos> condition) {
        this.condition = condition;
    }

    public static Codec<FishingCondition> CODEC = StringRepresentable.fromEnum(FishingCondition::values);

    public boolean test(ServerLevel level, BlockPos blockPos) {
        return condition.test(level, blockPos);
    }

    @Override
    public @NonNull String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
