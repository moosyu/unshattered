package io.github.moosyu.attachments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class PlayerCurrencyAttachment {
    public int coins;
    public int motes;

    PlayerCurrencyAttachment(int coins, int motes) {
        this.coins = coins;
        this.motes = motes;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void addCoins(int amount) {
        coins += amount;
    }

    public void removeCoins(int amount) {
        coins -= amount;
    }

    public int getMotes() {
        return motes;
    }

    public void setMotes(int motes) {
        this.motes = motes;
    }

    public static final Codec<PlayerCurrencyAttachment> RECORD_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("coins").forGetter(PlayerCurrencyAttachment::getCoins),
                    Codec.INT.fieldOf("motes").forGetter(PlayerCurrencyAttachment::getMotes)
            ).apply(instance, PlayerCurrencyAttachment::new)
    );

    public static final StreamCodec<ByteBuf, PlayerCurrencyAttachment> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, PlayerCurrencyAttachment::getCoins,
            ByteBufCodecs.INT, PlayerCurrencyAttachment::getMotes,
            PlayerCurrencyAttachment::new
    );
}