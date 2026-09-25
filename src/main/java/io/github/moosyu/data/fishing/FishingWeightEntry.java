package io.github.moosyu.data.fishing;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

public record FishingWeightEntry(Optional<FishingConditions> condition, double weight, FishingTypes type, Optional<Integer> fishingLevelRequirement) {
    public FishingWeightEntry(double weight, FishingTypes type) {
        this(Optional.empty(), weight, type, Optional.empty());
    }

    public static Codec<FishingWeightEntry> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    FishingConditions.CODEC.optionalFieldOf("condition").forGetter(FishingWeightEntry::condition),
                    Codec.DOUBLE.fieldOf("weight").forGetter(FishingWeightEntry::weight),
                    FishingTypes.CODEC.fieldOf("type").forGetter(FishingWeightEntry::type),
                    Codec.INT.optionalFieldOf("level_requirement").forGetter(FishingWeightEntry::fishingLevelRequirement)
            ).apply(instance, FishingWeightEntry::new)
    );
}
