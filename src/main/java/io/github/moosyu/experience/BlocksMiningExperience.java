package io.github.moosyu.experience;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Map;

public class BlocksMiningExperience {
    private static final Map<Block, Float> MINING_EXP = Map.ofEntries(
            Map.entry(Blocks.STONE, 1.0f),
            Map.entry(Blocks.COBBLESTONE, 1.0f),
            Map.entry(Blocks.COAL_ORE, 5.0f),
            Map.entry(Blocks.GOLD_ORE, 6.0f),
            Map.entry(Blocks.LAPIS_ORE, 7.0f),
            Map.entry(Blocks.REDSTONE_ORE, 7.0f),
            Map.entry(Blocks.EMERALD_ORE, 9.0f),
            Map.entry(Blocks.DIAMOND_ORE, 10.0f),
            Map.entry(Blocks.COAL_BLOCK, 20.0f),
            Map.entry(Blocks.IRON_BLOCK, 20.0f),
            Map.entry(Blocks.GOLD_BLOCK, 20.0f),
            Map.entry(Blocks.LAPIS_BLOCK, 20.0f),
            Map.entry(Blocks.REDSTONE_BLOCK, 20.0f),
            Map.entry(Blocks.EMERALD_BLOCK, 20.0f),
            Map.entry(Blocks.DIAMOND_BLOCK, 20.0f),
            Map.entry(Blocks.NETHER_QUARTZ_ORE, 5.0f),
            Map.entry(Blocks.QUARTZ_BLOCK, 18.0f),
            Map.entry(Blocks.END_STONE, 3.0f),
            Map.entry(Blocks.OBSIDIAN, 20.0f),
            Map.entry(Blocks.NETHERRACK, 0.5f),
            Map.entry(Blocks.ICE, 0.5f),
            Map.entry(Blocks.GLOWSTONE, 7.0f),
            Map.entry(Blocks.RED_SAND, 3.0f),
            Map.entry(Blocks.MYCELIUM, 3.0f),
            Map.entry(Blocks.SAND, 3.0f),
            Map.entry(Blocks.GRAVEL, 1.0f)
    );

    public static float getExp(Block block) {
        return MINING_EXP.getOrDefault(block, 0.0f);
    }
}
