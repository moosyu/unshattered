package io.github.moosyu.data.regen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.moosyu.events.DataPackRegistryHandler;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public final class RegenPaths {
    /**
     * a path in which a block travels when broken, life is a highway and all that
     * @param path the list of blockstates the block reaches, at index 0 is its initial state
     * @param regenTicks the amount of ticks for the block to regen up between states
     * @param stagesIncremented the amount of stages that get moved through the block is broken, generally 1 but sometimes needed when some stages need to be regenerated up but not broken down (like wheat)
     * @param variant possible other block that could be regenerated up to on the final stage like titanium or giant crops
     * @param regenCondition condition required for path to regenerate
     */
    public record RegenPath(List<BlockState> path, int regenTicks, int stagesIncremented, Optional<RegenVariant> variant, Optional<RegenCondition> regenCondition) {
        public RegenPath(List<BlockState> path, int regenTicks) {
            this(path, regenTicks, 1, Optional.empty(), Optional.empty());
        }

        public static final Codec<RegenPath> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        BlockState.CODEC.listOf().fieldOf("path").forGetter(RegenPath::path),
                        Codec.INT.fieldOf("regen_ticks").forGetter(RegenPath::regenTicks),
                        Codec.INT.fieldOf("stages_incremented").forGetter(RegenPath::stagesIncremented),
                        RegenVariant.CODEC.optionalFieldOf("variant").forGetter(RegenPath::variant),
                        RegenCondition.CODEC.optionalFieldOf("condition").forGetter(RegenPath::regenCondition)
                ).apply(instance, RegenPath::new)
        );

        public record RegenVariant(BlockState blockState, float chance) {
            public static final Codec<RegenVariant> CODEC = RecordCodecBuilder.create(instance ->
                    instance.group(
                            BlockState.CODEC.fieldOf("blockState").forGetter(RegenVariant::blockState),
                            Codec.FLOAT.fieldOf("chance").forGetter(RegenVariant::chance)
                    ).apply(instance, RegenVariant::new)
            );
        }

        public BlockState rollRestoredState(Level level, RegenSavedData.RegenState regenState) {
            if (variant().isPresent()) {
                BlockState variantBlockstate = variant.get().blockState;
                float spawnChance = variant.get().chance;
                if (variantBlockstate.getBlock() instanceof ChanceModifiedVariant modifiedVariant) {
                    MinecraftServer server = level.getServer();

                    if (server != null && regenState.getBrokenByUUID().isPresent()) {
                        ServerPlayer breaker = level.getServer().getPlayerList().getPlayer(regenState.getBrokenByUUID().get());
                        if (breaker != null) {
                            spawnChance *= modifiedVariant.getModifiedRollChance(breaker);
                        }
                    }
                }

                if (level.getRandom().nextFloat() <= spawnChance) {
                    return variantBlockstate;
                }
            }

            return path.getFirst();
        }
    }

    private static final Map<Registry<RegenPath>, Map<BlockState, Identifier>> LOOKUP_CACHE = new ConcurrentHashMap<>();

    /**
     * @return the regen path identifier whose first state is the given blockstate or null
     */
    public static @Nullable Identifier getRegenIdentifier(RegistryAccess registryAccess, BlockState state) {
        Registry<RegenPath> registry = registryAccess.lookupOrThrow(DataPackRegistryHandler.REGEN_PATH_REGISTRY_KEY);
        return LOOKUP_CACHE.computeIfAbsent(registry, pathRegistry -> {
            Map<BlockState, Identifier> map = new HashMap<>();
            pathRegistry.listElements().forEach(ref -> {
                RegenPath path = ref.value();
                Stream.concat(Stream.of(path.path().getFirst()), path.variant.stream().map(RegenPath.RegenVariant::blockState))
                        .forEach(blockState -> {
                            Identifier previous = map.put(blockState, ref.key().identifier());
                            if (previous != null) {
                                throw new RuntimeException("regen state " + blockState + " in path " + ref.key().identifier() + " duplicates path " + previous);
                            }
                        });
            });
            return map;
        }).get(state);
    }
}
