package io.github.moosyu.data.fishing;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.NonNull;

import java.util.Locale;

public enum FishingTypes implements StringRepresentable {
    WATER,
    LAVA;

    public static Codec<FishingTypes> CODEC = StringRepresentable.fromEnum(FishingTypes::values);

    @Override
    public @NonNull String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
