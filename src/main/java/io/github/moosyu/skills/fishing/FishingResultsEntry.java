package io.github.moosyu.skills.fishing;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record FishingResultsEntry(String id, String type, int levelRequirement, float exp) {
    public static final Codec<FishingResultsEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("id").forGetter(FishingResultsEntry::id),
            Codec.STRING.fieldOf("type").forGetter(FishingResultsEntry::type),
            Codec.INT.fieldOf("levelRequirement").forGetter(FishingResultsEntry::levelRequirement),
            Codec.FLOAT.fieldOf("exp").forGetter(FishingResultsEntry::exp)
    ).apply(instance, FishingResultsEntry::new));
}
