package io.github.moosyu.data.fishing;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public record FishingWeightEntry(Optional<List<FishingCondition>> conditions, double weight, Optional<Integer> fishingLevelRequirement) implements FishingEntry {
    public FishingWeightEntry(double weight, FishingCondition... conditions) {
        this(Optional.of(Arrays.stream(conditions).toList()), weight, Optional.empty());
    }

    public FishingWeightEntry(double weight, int fishingLevelRequirement, FishingCondition... conditions) {
        this(Optional.of(Arrays.stream(conditions).toList()), weight, Optional.of(fishingLevelRequirement));
    }

    public static Codec<FishingWeightEntry> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    FishingCondition.CODEC.listOf().optionalFieldOf("conditions").forGetter(FishingWeightEntry::conditions),
                    Codec.DOUBLE.fieldOf("weight").forGetter(FishingWeightEntry::weight),
                    Codec.INT.optionalFieldOf("level_requirement").forGetter(FishingWeightEntry::fishingLevelRequirement)
            ).apply(instance, FishingWeightEntry::new)
    );
}
