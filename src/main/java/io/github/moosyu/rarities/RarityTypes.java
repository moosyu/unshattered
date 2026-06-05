package io.github.moosyu.rarities;

import com.mojang.serialization.Codec;
import io.github.moosyu.helpers.OpacityHelper;

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

    // https://stackoverflow.com/a/28483738 my goodness
    public int getColor(float opacity) {
        return OpacityHelper.getOpacityColor(color, opacity);
    }
    public static final Codec<RarityTypes> CODEC = Codec.STRING.xmap(RarityTypes::valueOf, RarityTypes::name);
}
