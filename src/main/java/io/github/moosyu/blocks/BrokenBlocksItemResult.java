package io.github.moosyu.blocks;

import io.github.moosyu.items.ItemsRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Map;

public final class BrokenBlocksItemResult {
    private static final Map<Block, Item> BROKEN_BLOCKS_ITEM_RESULT = Map.ofEntries(
            Map.entry(BlocksRegistry.BREAKABLE_STONE_BLOCK.get(), Items.COBBLESTONE),
            Map.entry(BlocksRegistry.BREAKABLE_COBBLESTONE_BLOCK.get(), Items.COBBLESTONE),
            Map.entry(BlocksRegistry.BREAKABLE_FIG_LOG_BLOCK.get(), ItemsRegistry.FIG_LOG.asItem())
    );

    public static Item getItemDropped(Block block) {
        return BROKEN_BLOCKS_ITEM_RESULT.getOrDefault(block, block.asItem());
    }
}
