package io.github.moosyu.data;

import org.jspecify.annotations.NonNull;

public enum DropTypes {
    COMMON("common", 0xFFFFFFFF, 0.2f),
    OCCASIONAL("occasional", 0xFF00AA00, 0.05f),
    RARE("rare", 0xFFFFAA00, 0.01f),
    CRAZY_RARE("crazy_rare", 0xFFA335EE, 0.005f),
    PRAY_RNGESUS("pray_rngesus", 0xFFFF55FF, 0.0005f),
    RNGESUS_INCARNATE("rngesus_incarnate", 0xFFFF5555, 0.0f);

    public final String key;
    public final int colour;
    public final float minRate;

    DropTypes(String key, int colour, float minRate) {
        this.key = key;
        this.colour = colour;
        this.minRate = minRate;
    }

    /**
     * get the drop type based on drop chance
     * @param dropChance probably use the value of it modified by combat fortune in most situations
     * @return the drop type
     */
    public static @NonNull DropTypes getDropType(float dropChance) {
        DropTypes type;

        if (dropChance >= DropTypes.COMMON.minRate) {
            type = COMMON;
        } else if (dropChance >= DropTypes.OCCASIONAL.minRate) {
            type = OCCASIONAL;
        } else if (dropChance >= DropTypes.RARE.minRate) {
            type = RARE;
        } else if (dropChance >= DropTypes.CRAZY_RARE.minRate) {
            type = CRAZY_RARE;
        } else if (dropChance >= DropTypes.PRAY_RNGESUS.minRate) {
            type = PRAY_RNGESUS;
        } else {
            type = RNGESUS_INCARNATE;
        }
        return type;
    }
}
