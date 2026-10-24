package io.github.moosyu.data.fishing;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.NonNull;

import java.util.Locale;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public enum FishingCondition implements StringRepresentable {
    NONE((_, _) -> true),
    NIGHT((level, _) -> level.isDarkOutside());

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
