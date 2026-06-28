package io.github.moosyu.skills.fishing;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record FishingResultsEntry(String id, String type, int levelRequirement, float exp, int weight, List<FishingCondition> conditions) {
    public static final Codec<FishingResultsEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("id").forGetter(FishingResultsEntry::id),
            Codec.STRING.fieldOf("type").forGetter(FishingResultsEntry::type),
            Codec.INT.fieldOf("levelRequirement").forGetter(FishingResultsEntry::levelRequirement),
            Codec.FLOAT.fieldOf("exp").forGetter(FishingResultsEntry::exp),
            Codec.INT.fieldOf("weight").forGetter(FishingResultsEntry::weight),
            FishingCondition.CODEC.listOf().optionalFieldOf("conditions", List.of()).forGetter(FishingResultsEntry::conditions)
    ).apply(instance, FishingResultsEntry::new));
}
