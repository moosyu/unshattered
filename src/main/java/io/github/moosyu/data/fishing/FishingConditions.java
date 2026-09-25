package io.github.moosyu.data.fishing;

import com.mojang.serialization.Codec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.NonNull;

import java.util.Locale;
import java.util.function.Predicate;

public enum FishingConditions implements StringRepresentable {
    NONE(_ -> true),
    NIGHT(player -> player.level().isDarkOutside());

    final Predicate<ServerPlayer> condition;
    FishingConditions(Predicate<ServerPlayer> condition) {
        this.condition = condition;
    }

    public static Codec<FishingConditions> CODEC = StringRepresentable.fromEnum(FishingConditions::values);

    @Override
    public @NonNull String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
