package io.github.moosyu.rarities;

import com.mojang.serialization.Codec;
import io.github.moosyu.util.GetOpacity;

public enum RarityTypes {
    COMMON(0xFFFFFF),
    UNCOMMON(0x55FF55),
    RARE(0x5555FF),
    EPIC(0x810AF3),
    LEGENDARY(0xFFAA00),
    MYTHIC(0xFF55FF),
    DIVINE(0x55FFFF),
    SPECIAL(0xFF5555);

    private final int color;

    RarityTypes(int color) {
        this.color = color;
    }

    public int getColor(float opacity) {
        return GetOpacity.getOpacityColor(color, opacity);
    }
    public static final Codec<RarityTypes> CODEC = Codec.STRING.xmap(RarityTypes::valueOf, RarityTypes::name);
}
