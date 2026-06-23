package io.github.moosyu.skills.fishing;

import io.github.moosyu.Unshattered;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Map;

public final class FishingManager extends SimpleJsonResourceReloadListener<FishingResultsEntries> {
    private static Map<Identifier, FishingResultsEntries> objects = Map.of();

    public FishingManager() {
        super(FishingResultsEntries.CODEC, FileToIdConverter.json("fishing_results"));
    }

    @Override
    protected void apply(@NonNull Map<Identifier, FishingResultsEntries> identifierFishingResultsMap, @NonNull ResourceManager resourceManager, @NonNull ProfilerFiller profilerFiller) {
        objects = Map.copyOf(identifierFishingResultsMap);
    }

    public static List<FishingResultsEntry> getEntries(Identifier resultsIdentifier) {
        if (objects.get(resultsIdentifier) == null) {
            Unshattered.LOGGER.error("this entry couldnt be found...");
        }
        return objects.get(resultsIdentifier).entries();
    }
}
