package io.github.moosyu.skills.fishing;

import io.github.moosyu.conditions.TimeConditions;

import java.util.Optional;

public record FishingResultsConditions(Optional<TimeConditions> timeConditions) {
//    public static final Codec<FishingResultsConditions> CODEC = RecordCodecBuilder.create(instance -> // Given an instance
//            instance.group(
//                    Codec.BOOL.optionalFieldOf("time_conditions").forGetter(FishingResultsConditions::timeConditions),
//            ).apply(instance, FishingResultsConditions::new)
//    );
}
