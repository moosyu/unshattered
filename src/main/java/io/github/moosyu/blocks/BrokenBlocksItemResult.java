package io.github.moosyu.blocks;

import io.github.moosyu.items.UnshatteredItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

import java.util.Map;

public final class BrokenBlocksItemResult {
    private static final Map<Block, Item> BROKEN_BLOCKS_ITEM_RESULT = Map.ofEntries(
            Map.entry(UnshatteredBlocks.BREAKABLE_STONE_BLOCK.get(), Items.COBBLESTONE),
            Map.entry(UnshatteredBlocks.BREAKABLE_COBBLESTONE_BLOCK.get(), Items.COBBLESTONE),
            Map.entry(UnshatteredBlocks.BREAKABLE_FIG_LOG_BLOCK.get(), UnshatteredItems.FIG_LOG.asItem())
    );

    public static Item getItemDropped(Block block) {
        return BROKEN_BLOCKS_ITEM_RESULT.getOrDefault(block, block.asItem());
    }
}
