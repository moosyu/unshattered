package io.github.moosyu.data.drops;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.Nullable;

public record CoinReward(int min, int max) implements DropReward {
    public static final MapCodec<CoinReward> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("min").forGetter(CoinReward::min),
            Codec.INT.fieldOf("max").forGetter(CoinReward::max)
    ).apply(instance, CoinReward::new));

    @Override public Type type() {
        return Type.COINS;
    }

    @Override
    public float grant(ServerPlayer player, RandomSource random, @Nullable Double fortuneAmount, float modifiedPercentage) {
        int count = random.nextIntBetweenInclusive(min, max);

        player.getData(UnshatteredAttachments.PLAYER_CURRENCY.get()).addCoins((int) (count * modifiedPercentage));
        player.syncData(UnshatteredAttachments.PLAYER_CURRENCY.get());

        return count;
    }
}
