package io.github.moosyu.entities.projectile.terrain;

import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class YetiTossShape {
    // prebuilt offsets to loop through when spawning blocks in the shape of that tossed by yetis (or players)
    public static final List<BlockPos> OFFSETS = new ArrayList<>();

    static {
        for (int y = 0; y >= -2; y--) {
            int radius = 2 + y;
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    if (Math.abs(x) + Math.abs(z) <= radius) {
                        OFFSETS.add(new BlockPos(x, y, z));
                    }
                }
            }
        }
    }
}