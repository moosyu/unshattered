package io.github.moosyu.data.regen;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import io.github.moosyu.data.regen.conditions.NotBelowBlock;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.resources.Identifier;

import java.util.Optional;
import java.util.function.Function;

public class RegenConditionTypes {
    private static final BiMap<Identifier, MapCodec<? extends RegenCondition>> REGEN_CONDITION_TYPE_CODECS = HashBiMap.create();

    static {
        REGEN_CONDITION_TYPE_CODECS.put(UnshatteredUtils.getUnshatteredIdentifier("check_below"), NotBelowBlock.CODEC);
    }

    private static final Codec<MapCodec<? extends RegenCondition>> TYPE_CODEC = Identifier.CODEC.comapFlatMap(
            id -> Optional.ofNullable(REGEN_CONDITION_TYPE_CODECS.get(id))
                    .map(DataResult::success)
                    .orElseGet(() -> DataResult.error(() -> "unknown dialogue event type: " + id)),
            codec -> REGEN_CONDITION_TYPE_CODECS.inverse().get(codec)
    );

    public static final Codec<RegenCondition> CODEC = TYPE_CODEC.dispatch("type", RegenCondition::codec, Function.identity());
}
