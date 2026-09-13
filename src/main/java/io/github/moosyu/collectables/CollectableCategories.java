package io.github.moosyu.collectables;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.NonNull;

public enum CollectableCategories implements StringRepresentable {
    FARMING,
    MINING,
    COMBAT,
    FORAGING,
    FISHING;

    @Override
    public @NonNull String getSerializedName() {
        return this.toString().toLowerCase();
    }

    public static Codec<CollectableCategories> CODEC = StringRepresentable.fromEnum(CollectableCategories::values);
}
