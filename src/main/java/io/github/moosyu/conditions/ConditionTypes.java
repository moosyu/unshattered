package io.github.moosyu.conditions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.github.moosyu.skills.fishing.FishingCondition;

import java.util.HashMap;
import java.util.Map;

public class ConditionTypes {
    public static final Map<String, FishingCondition.ConditionType<? extends FishingCondition>> KEY_MAP = new HashMap<>();

    public static final Codec<FishingCondition.ConditionType<? extends FishingCondition>> TYPE_CODEC = Codec.stringResolver(
            type -> KEY_MAP.entrySet().stream()
                    .filter(e -> e.getValue() == type)
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse("unknown"),
            KEY_MAP::get
    );

    public static final FishingCondition.ConditionType<TimeConditions> TIME = register("time", TimeConditions.MAP_CODEC);

    private static <T extends FishingCondition> FishingCondition.ConditionType<T> register(String id, MapCodec<T> codec) {
        FishingCondition.ConditionType<T> type = new FishingCondition.ConditionType<>(codec);
        KEY_MAP.put(id, type);
        return type;
    }
}
