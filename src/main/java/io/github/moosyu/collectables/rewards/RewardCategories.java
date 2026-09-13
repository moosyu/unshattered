package io.github.moosyu.collectables.rewards;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.NonNull;

public enum RewardCategories implements StringRepresentable {
    ITEM("item", ItemCollectableReward.CODEC),
    EXPERIENCE("experience", ExperienceCollectableReward.CODEC);

    private final String id;
    private final MapCodec<? extends CollectableReward> codec;

    RewardCategories(String id, MapCodec<? extends CollectableReward> codec) {
        this.id = id;
        this.codec = codec;
    }

    public MapCodec<? extends CollectableReward> codec() {
        return codec;
    }

    @Override
    public @NonNull String getSerializedName() {
        return id;
    }

    public static final Codec<RewardCategories> CODEC = StringRepresentable.fromEnum(RewardCategories::values);
}