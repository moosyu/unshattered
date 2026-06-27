package io.github.moosyu.skills.fishing;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record FishingResultsEntries(List<FishingResultsEntry> entries) {
    public static final Codec<FishingResultsEntries> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            FishingResultsEntry.CODEC.listOf().fieldOf("entries").forGetter(FishingResultsEntries::entries)
        ).apply(instance, FishingResultsEntries::new)
    );
}
