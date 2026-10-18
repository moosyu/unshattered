package io.github.moosyu.items;

import com.mojang.serialization.Codec;
import io.github.moosyu.util.UnshatteredUtils;

public enum UnshatteredRarity {
    COMMON(0xFFFFFF),
    UNCOMMON(0x55FF55),
    RARE(0x5555FF),
    EPIC(0x810AF3),
    LEGENDARY(0xFFAA00),
    MYTHIC(0xFF55FF),
    DIVINE(0x55FFFF),
    SPECIAL(0xFF5555);

    private final int color;

    UnshatteredRarity(int color) {
        this.color = color;
    }

    public int getColour() {
        return color;
    }

    public static final Codec<UnshatteredRarity> CODEC = Codec.STRING.xmap(UnshatteredRarity::valueOf, UnshatteredRarity::name);
}
