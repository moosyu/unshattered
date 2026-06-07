package io.github.moosyu.data;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static io.github.moosyu.Unshattered.MODID;

// google ai summary had just as much to do with this code as i did so im quite sure some of it is going to cause trouble down the road
// sorry but when i realized id have to deal with codecs again i just couldnt stomach the thought.
public class RegenBlocksSavedData extends SavedData {
    private static final Codec<Map<BlockPos, Long>> MAP_CODEC = Codec.list(
            RecordCodecBuilder.<Pair<BlockPos, Long>>create(instance -> instance.group(
                    BlockPos.CODEC.fieldOf("pos").forGetter(Pair::getFirst),
                    Codec.LONG.fieldOf("tick").forGetter(Pair::getSecond)
            ).apply(instance, Pair::of))).xmap(
            list -> list.stream().collect(Collectors.toMap(Pair::getFirst, Pair::getSecond)),
            map -> map.entrySet().stream().map(e -> Pair.of(e.getKey(), e.getValue())).toList()
    );

    public static final SavedDataType<RegenBlocksSavedData> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath(MODID, "regen_blocks"),
            _ -> new RegenBlocksSavedData(new HashMap<>()),
            _ -> MAP_CODEC.xmap(RegenBlocksSavedData::new, data -> data.regenTicks)
    );

    private final Map<BlockPos, Long> regenTicks;

    private RegenBlocksSavedData(Map<BlockPos, Long> regenTicks) {this.regenTicks = new HashMap<>(regenTicks);}

    public static RegenBlocksSavedData get(ServerLevel level) {return level.getDataStorage().computeIfAbsent(TYPE);}

    public void addBlock(BlockPos pos, long regenTick) {
        this.regenTicks.put(pos, regenTick);
        this.setDirty();
    }

    public void removeBlock(BlockPos pos) {if (this.regenTicks.remove(pos) != null) this.setDirty();}

    public Map<BlockPos, Long> getRegenTicks() {return Collections.unmodifiableMap(regenTicks);}
}