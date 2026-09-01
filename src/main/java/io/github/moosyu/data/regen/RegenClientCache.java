package io.github.moosyu.data.regen;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class RegenClientCache {
    private static final Map<BlockPos, State> CACHE = new HashMap<>();
    public record State(ResourceKey<RegenPaths.RegenPath> pathIdentifier, int index) {}

    public static void put(BlockPos pos, ResourceKey<RegenPaths.RegenPath> pathIdentifier, int index) {
        CACHE.put(pos.immutable(), new State(pathIdentifier, index));
    }

    @Nullable
    public static State get(BlockPos pos) {
        return CACHE.get(pos);
    }

    public static void clear() {
        CACHE.clear();
    }

    public static void remove(BlockPos pos) {
        CACHE.remove(pos);
    }
}
