package io.github.moosyu.conditions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.moosyu.skills.fishing.FishingCondition;
import io.github.moosyu.skills.fishing.FishingContext;

import java.util.Locale;

public record TimeConditions(TimeTypes timeValue) implements FishingCondition {
    private static final Codec<TimeTypes> CODEC = Codec.stringResolver(
        type -> type.name().toLowerCase(Locale.ROOT),
        string -> {
            try {
                return TimeTypes.valueOf(string.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    );

    public static final MapCodec<TimeConditions> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            CODEC.fieldOf("value").forGetter(TimeConditions::timeValue)
    ).apply(instance, TimeConditions::new));

    @Override
    public boolean test(FishingContext context) {
        return switch (timeValue) {
            case DAY -> context.level().isBrightOutside();
            case NIGHT -> context.level().isDarkOutside();
        };
    }

    @Override
    public ConditionType<?> getType() {
        return ConditionTypes.TIME;
    }
}