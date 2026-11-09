package io.github.moosyu.data.drops;

import org.jspecify.annotations.NonNull;

public enum DropType {
    COMMON("common", 0xFFFFFFFF, 0.2f),
    OCCASIONAL("occasional", 0xFF00AA00, 0.05f),
    RARE("rare", 0xFFFFAA00, 0.01f),
    CRAZY_RARE("crazy_rare", 0xFFA335EE, 0.005f),
    PRAY_RNGESUS("pray_rngesus", 0xFFFF55FF, 0.0005f),
    RNGESUS_INCARNATE("rngesus_incarnate", 0xFFFF5555, 0.0f);

    public final String key;
    public final int colour;
    public final float minRate;

    DropType(String key, int colour, float minRate) {
        this.key = key;
        this.colour = colour;
        this.minRate = minRate;
    }

    /**
     * get the drop type based on drop chance
     * @param dropChance drop chance of the item (unmodified)
     * @return the drop type
     */
    public static @NonNull DropType getDropType(float dropChance) {
        DropType type;

        if (dropChance >= DropType.COMMON.minRate) {
            type = COMMON;
        } else if (dropChance >= DropType.OCCASIONAL.minRate) {
            type = OCCASIONAL;
        } else if (dropChance >= DropType.RARE.minRate) {
            type = RARE;
        } else if (dropChance >= DropType.CRAZY_RARE.minRate) {
            type = CRAZY_RARE;
        } else if (dropChance >= DropType.PRAY_RNGESUS.minRate) {
            type = PRAY_RNGESUS;
        } else {
            type = RNGESUS_INCARNATE;
        }
        return type;
    }
}
