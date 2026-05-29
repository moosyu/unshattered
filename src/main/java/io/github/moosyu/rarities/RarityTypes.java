package io.github.moosyu.rarities;

import com.mojang.serialization.Codec;

public enum RarityTypes {
    COMMON(0xFFFFFFFF),
    UNCOMMON(0xFF55FF55),
    RARE(0xFF5555FF),
    EPIC(0xFFAA00AA),
    LEGENDARY(0xFFFFAA00),
    MYTHIC(0xFFFF55FF),
    DIVINE(0xFF55FFFF),
    SPECIAL(0xFFFF5555);

    private final int color;

    RarityTypes(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }
    public static final Codec<RarityTypes> CODEC = Codec.STRING.xmap(RarityTypes::valueOf, RarityTypes::name);
}
