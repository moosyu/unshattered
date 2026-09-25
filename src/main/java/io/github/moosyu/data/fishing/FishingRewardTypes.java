package io.github.moosyu.data.fishing;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import io.github.moosyu.data.fishing.rewards.CoinReward;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.resources.Identifier;

import java.util.Optional;
import java.util.function.Function;

public final class FishingRewardTypes {
    // both codecs and identifiers SHOULD be unique. if they aren't there probably shouldn't be two reward types anyways.
    private static final BiMap<Identifier, MapCodec<? extends TriggerMiscReward>> FISHING_REWARD_TYPE_CODECS = HashBiMap.create();

    static {
        FISHING_REWARD_TYPE_CODECS.put(UnshatteredUtils.getUnshatteredIdentifier("give_coins"), CoinReward.CODEC);
    }

    private static final Codec<MapCodec<? extends TriggerMiscReward>> TYPE_CODEC = Identifier.CODEC.comapFlatMap(
            id -> Optional.ofNullable(FISHING_REWARD_TYPE_CODECS.get(id))
                    .map(DataResult::success)
                    .orElseGet(() -> DataResult.error(() -> "unknown dialogue event type: " + id)),
            codec -> FISHING_REWARD_TYPE_CODECS.inverse().get(codec)
    );

    public static final Codec<TriggerMiscReward> CODEC = TYPE_CODEC.dispatch("type", TriggerMiscReward::codec, Function.identity());
}
