package io.github.moosyu.rarities;

import com.mojang.serialization.Codec;
import io.github.moosyu.util.UnshatteredUtils;

public enum UnshatteredRarities {
    COMMON(0xFFFFFF),
    UNCOMMON(0x55FF55),
    RARE(0x5555FF),
    EPIC(0x810AF3),
    LEGENDARY(0xFFAA00),
    MYTHIC(0xFF55FF),
    DIVINE(0x55FFFF),
    SPECIAL(0xFF5555);

    private final int color;

    UnshatteredRarities(int color) {
        this.color = color;
    }

    /**
     *
     * @param opacity 0.0 -> 1.0, why did i include opacity here? whos to say.
     * @return the colour
     */
    public int getColour(float opacity) {
        return UnshatteredUtils.getOpacityColor(color, opacity);
    }

    public static final Codec<UnshatteredRarities> CODEC = Codec.STRING.xmap(UnshatteredRarities::valueOf, UnshatteredRarities::name);
}
