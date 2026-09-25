package io.github.moosyu.data.fishing;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

public record FishingWeightEntry(Optional<FishingConditions> condition, double weight, Optional<Integer> fishingLevelRequirement) implements FishingEntry {
    public FishingWeightEntry(double weight) {
        this(Optional.empty(), weight, Optional.empty());
    }

    public static Codec<FishingWeightEntry> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    FishingConditions.CODEC.optionalFieldOf("condition").forGetter(FishingWeightEntry::condition),
                    Codec.DOUBLE.fieldOf("weight").forGetter(FishingWeightEntry::weight),
                    Codec.INT.optionalFieldOf("level_requirement").forGetter(FishingWeightEntry::fishingLevelRequirement)
            ).apply(instance, FishingWeightEntry::new)
    );
}
