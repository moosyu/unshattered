package io.github.moosyu.damage;

import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

public enum DamageType {
    NORMAL(UnshatteredUtils.WHITE),
    CRITICAL(UnshatteredUtils.YELLOW),
    FEROCITY(UnshatteredUtils.RED);

    public final int colour;

    DamageType(int colour) {
        this.colour = colour;
    }

    public static final StreamCodec<FriendlyByteBuf, DamageType> STREAM_CODEC = NeoForgeStreamCodecs.enumCodec(DamageType.class);
}
