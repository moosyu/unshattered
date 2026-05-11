package io.github.moosyu.experience;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Map;

public class BlocksFarmingExperience {
    private static final Map<Block, Float> MINING_EXP = Map.ofEntries(
            Map.entry(Blocks.WHEAT, 4.0f),
            Map.entry(Blocks.POTATOES, 4.0f),
            Map.entry(Blocks.CARROTS, 4.0f),
            Map.entry(Blocks.PUMPKIN, 4.5f),
            Map.entry(Blocks.MELON, 4.0f),
            Map.entry(Blocks.SUGAR_CANE, 2.0f),
            Map.entry(Blocks.MUSHROOM_STEM, 2.0f),
            Map.entry(Blocks.BROWN_MUSHROOM_BLOCK, 2.0f),
            Map.entry(Blocks.BROWN_MUSHROOM, 6.0f),
            Map.entry(Blocks.RED_MUSHROOM_BLOCK, 2.0f),
            Map.entry(Blocks.RED_MUSHROOM, 6.0f),
            Map.entry(Blocks.CACTUS, 2.0f),
            Map.entry(Blocks.COCOA, 4.0f),
            Map.entry(Blocks.NETHER_WART, 4.0f)
    );

    public static float getExp(Block block) {
        return MINING_EXP.getOrDefault(block, 0.0f);
    }
}
