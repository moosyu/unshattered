package io.github.moosyu.data.regen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.moosyu.blocks.UnshatteredBlocks;
import io.github.moosyu.events.DataPackRegistryHandler;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public final class RegenPaths {
    /**
     * a path in which a block travels when broken, life is a highway and all that
     * @param path the list of blockstates the block reaches, at index 0 is its initial state
     * @param regenTicks the amount of ticks for the block to regen up between states
     * @param stagesIncremented the amount of stages that get moved through the block is broken, generally 1 but sometimes needed when some stages need to be regenerated up but not broken down (like wheat)
     */
    public record RegenPath(List<BlockState> path, int regenTicks, int stagesIncremented) {
        public RegenPath(List<BlockState> path, int regenTicks) {
            this(path, regenTicks, 1);
        }

        public static final Codec<RegenPath> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        BlockState.CODEC.listOf().fieldOf("path").forGetter(RegenPath::path),
                        Codec.INT.fieldOf("regen_ticks").forGetter(RegenPath::regenTicks),
                        Codec.INT.fieldOf("stages_incremented").forGetter(RegenPath::stagesIncremented)
                ).apply(instance, RegenPath::new)
        );
    }

    public static Map<BlockState, Identifier> REGEN_IDENTIFIER_BY_BLOCK;
    private static final Map<Registry<RegenPath>, Map<BlockState, Identifier>> LOOKUP_CACHE = new ConcurrentHashMap<>();

    /**
     * @return the regen path identifier whose first state is the given blockstate or null
     */
    public static @Nullable Identifier getRegenIdentifier(RegistryAccess registryAccess, BlockState state) {
        Registry<RegenPath> registry = registryAccess.lookupOrThrow(DataPackRegistryHandler.REGEN_PATH_REGISTRY_KEY);
        return LOOKUP_CACHE.computeIfAbsent(registry, pathRegistry -> pathRegistry.listElements()
                .collect(Collectors.toMap(
                        ref -> ref.value().path().getFirst(),
                        ref -> ref.key().identifier(),
                        (identifier, _) -> {
                            throw new RuntimeException("regen identifier " + identifier + " was duplicated");
                        }
                ))
        ).get(state);
    }
}
