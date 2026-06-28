package io.github.moosyu.skills.fishing;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.github.moosyu.conditions.ConditionTypes;

public interface FishingCondition {
    Codec<FishingCondition> CODEC = ConditionTypes.TYPE_CODEC.dispatch(FishingCondition::getType, ConditionType::codec);

    boolean test(FishingContext context);
    ConditionType<?> getType();

    record ConditionType<T extends FishingCondition>(MapCodec<T> codec) {}
}