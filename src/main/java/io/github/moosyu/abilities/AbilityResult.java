package io.github.moosyu.abilities;

import java.util.function.BinaryOperator;

public record AbilityResult<T>(Class<T> type, T defaultValue, BinaryOperator<T> combiner) {
    public static final AbilityResult<Boolean> CANCEL_EVENT = new AbilityResult<>(Boolean.class, false, Boolean::logicalOr);
    public static final AbilityResult<Boolean> CANCEL_COIN_LOSS = new AbilityResult<>(Boolean.class, false, Boolean::logicalOr);
    public static final AbilityResult<Float> MODIFY_COOLDOWN_AMOUNT = new AbilityResult<>(Float.class, 0f, Float::sum);
}