package io.github.moosyu.abilities;

import io.github.moosyu.data.fishing.FishingWeightModifier;

import java.util.function.BinaryOperator;

public record AbilityResult<T>(Class<T> type, T defaultValue, BinaryOperator<T> combiner) {
    public static final AbilityResult<Boolean> CANCEL_EVENT = new AbilityResult<>(Boolean.class, false, Boolean::logicalOr);
    public static final AbilityResult<Boolean> CANCEL_COIN_LOSS = new AbilityResult<>(Boolean.class, false, Boolean::logicalOr);
    public static final AbilityResult<Float> MODIFY_COOLDOWN_AMOUNT = new AbilityResult<>(Float.class, 0f, Float::sum);
    public static final AbilityResult<FishingWeightModifier> MODIFY_FISHING_MOB_WEIGHTS = new AbilityResult<>(FishingWeightModifier.class, FishingWeightModifier.NONE, FishingWeightModifier::combine);
    public static final AbilityResult<FishingWeightModifier> MODIFY_FISHING_ITEM_WEIGHTS = new AbilityResult<>(FishingWeightModifier.class, FishingWeightModifier.NONE, FishingWeightModifier::combine);
    public static final AbilityResult<Float> ADD_PRESENT_EXPERIENCE_PERCENT = new AbilityResult<>(Float.class, 1.0f, AbilityResult::addPercentageFloats);
    public static final AbilityResult<Float> ADD_PRESENT_COINS_PERCENT = new AbilityResult<>(Float.class, 1.0f, AbilityResult::addPercentageFloats);

    private static float addPercentageFloats(float a, float b) {
        return a + b - 1.0f;
    }
}