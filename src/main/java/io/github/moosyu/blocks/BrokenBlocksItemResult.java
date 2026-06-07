package io.github.moosyu.blocks;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Map;

public final class BrokenBlocksItemResult {
    private static final Map<Block, Item> BROKEN_BLOCKS_ITEM_RESULT = Map.ofEntries(
            Map.entry(Blocks.STONE, Items.COBBLESTONE),
            Map.entry(Blocks.COBBLESTONE, Items.COBBLESTONE)
    );

    public static Item getItemDropped(Block block) {
        return BROKEN_BLOCKS_ITEM_RESULT.getOrDefault(block, block.asItem());
    }
}
