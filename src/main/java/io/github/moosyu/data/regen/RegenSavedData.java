package io.github.moosyu.data.regen;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.HashMap;
import java.util.Map;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.data.regen.RegenPaths.REGEN_IDENTIFIER_BY_BLOCK;
import static io.github.moosyu.data.regen.RegenPaths.REGEN_STAGES;
import static io.github.moosyu.data.regen.RegenPaths.RegenPath;

public class RegenSavedData extends SavedData {
    public static final class RegenState {
        private final String regenPathIdentifier;
        private final int regenPathIndex;
        private int ticksRemaining;

        public RegenState(String regenPathIdentifier, int regenPathIndex, int ticksRemaining) {
            this.regenPathIdentifier = regenPathIdentifier;
            this.regenPathIndex = regenPathIndex;
            this.ticksRemaining = ticksRemaining;
        }

        public String regenPathIdentifier() {
            return regenPathIdentifier;
        }

        public int regenPathIndex() {
            return regenPathIndex;
        }

        public int ticksRemaining() {
            return ticksRemaining;
        }

        public void decrementTicks() {
            ticksRemaining--;
        }

        public static final Codec<RegenState> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.STRING.fieldOf("regen_path_identifier")
                                .forGetter(RegenState::regenPathIdentifier),
                        Codec.INT.fieldOf("regen_path_index")
                                .forGetter(RegenState::regenPathIndex),
                        Codec.INT.fieldOf("ticks_remaining")
                                .forGetter(RegenState::ticksRemaining)
                ).apply(instance, RegenState::new)
        );
    }

    public final Map<GlobalPos, RegenState> regenQueue;

    public RegenSavedData() {
        regenQueue = new HashMap<>();
    }

    public RegenSavedData(Map<GlobalPos, RegenState> regenQueue) {
        this.regenQueue = new HashMap<>(regenQueue);
    }

    /**
     * attempt to destroy a block
     * @param blockPos global position of block being broken
     * @param level serverlevel of block being broken
     */
    public void destroyRegeneratingBlock(BlockPos blockPos, ServerLevel level) {
        GlobalPos globalPos = GlobalPos.of(level.dimension(), blockPos);

        if (regenQueue.containsKey(globalPos)) {
            RegenState state = regenQueue.get(globalPos);
            RegenPath regenPath = REGEN_STAGES.get(state.regenPathIdentifier);
            int newRegenPathIndex = state.regenPathIndex + 1;

            if (newRegenPathIndex < regenPath.path().size()) {
                level.setBlockAndUpdate(globalPos.pos(), regenPath.path().get(newRegenPathIndex));
                regenQueue.put(globalPos, new RegenState(state.regenPathIdentifier, newRegenPathIndex, regenPath.regenTicks()));
                this.setDirty();
            }
        } else {
            BlockState blockState = level.getBlockState(globalPos.pos());
            if (REGEN_IDENTIFIER_BY_BLOCK.containsKey(blockState)) {
                String regenerationIdentifier = REGEN_IDENTIFIER_BY_BLOCK.get(level.getBlockState(globalPos.pos()));
                RegenPath regenPath = REGEN_STAGES.get(regenerationIdentifier);

                level.setBlockAndUpdate(globalPos.pos(), regenPath.path().get(1));
                regenQueue.put(globalPos, new RegenState(regenerationIdentifier, 1, regenPath.regenTicks()));
                this.setDirty();
            }
        }
    }

    public void regenerateBlock(GlobalPos blockPos, ServerLevel level) {
        if (regenQueue.containsKey(blockPos)) {
            RegenState state = regenQueue.get(blockPos);
            int newRegenPathIndex = state.regenPathIndex - 1;
            RegenPath regenPath = REGEN_STAGES.get(state.regenPathIdentifier);
            BlockState newBlockstate = regenPath.path().get(newRegenPathIndex);

            level.setBlockAndUpdate(blockPos.pos(), newBlockstate);

            if (newRegenPathIndex == 0) {
                regenQueue.remove(blockPos);
            } else {
                regenQueue.put(blockPos, new RegenState(state.regenPathIdentifier, newRegenPathIndex, regenPath.regenTicks()));
            }

            this.setDirty();
        }
    }

    /**
     * purposefully doesnt include setDirty for performance
     * @param blockPos the position of the regenerating block
     * @return true if the block needs to be regenerated
     */
    public boolean tickBlock(GlobalPos blockPos) {
        if (regenQueue.containsKey(blockPos)) {
            regenQueue.get(blockPos).decrementTicks();
            return regenQueue.get(blockPos).ticksRemaining <= 0;
        }

        return false;
    }

    public static final Codec<Map<GlobalPos, RegenState>> REGEN_QUEUE_CODEC = Codec.pair(
            GlobalPos.CODEC.fieldOf("pos").codec(),
            RegenState.CODEC.fieldOf("state").codec()
    ).listOf().xmap(list -> {
        Map<GlobalPos, RegenState> map = new HashMap<>();
        for (Pair<GlobalPos, RegenState> pair : list) {
            map.put(pair.getFirst(), pair.getSecond());
        }
        return map;
        }, map -> map.entrySet().stream()
            .map(entry -> Pair.of(entry.getKey(), entry.getValue()))
            .toList()
    );

    public static final SavedDataType<RegenSavedData> ID = new SavedDataType<>(
            Identifier.fromNamespaceAndPath(MODID, "regen_data"),
            RegenSavedData::new,
            RecordCodecBuilder.create(instance -> instance.group(
                    REGEN_QUEUE_CODEC.fieldOf("regen_queue").forGetter(data -> data.regenQueue)
            ).apply(instance, RegenSavedData::new))
    );
}