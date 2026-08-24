package io.github.moosyu.data;

import io.github.moosyu.blocks.UnshatteredBlocks;
import io.github.moosyu.data.datagen.UnshatteredBlockTagsProvider;
import io.github.moosyu.data.datagen.UnshatteredItemTagsProvider;
import io.github.moosyu.items.UnshatteredItems;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NonNull;

import java.util.Set;

public class UnshatterredBlockLootSubProvider extends BlockLootSubProvider {
    public UnshatterredBlockLootSubProvider(HolderLookup.Provider lookupProvider) {
        super(Set.of(), FeatureFlags.DEFAULT_FLAGS, lookupProvider);
    }

    @Override
    protected void generate() {
        this.dropOther(UnshatteredBlocks.BREAKABLE_STONE_BLOCK.get(), Items.COBBLESTONE);
        this.dropOther(UnshatteredBlocks.BREAKABLE_COBBLESTONE_BLOCK.get(), Items.COBBLESTONE);
        this.dropOther(UnshatteredBlocks.BREAKABLE_FIG_LOG_BLOCK.get(), UnshatteredItems.FIG_LOG);
    }

    protected @NonNull Iterable<Block> getKnownBlocks() {
        return UnshatteredBlockTagsProvider.BREAKABLE_BLOCKS_LIST;
    }
}
