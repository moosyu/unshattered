package io.github.moosyu.data.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.blocks.UnshatteredBlocks.*;

public class UnshatteredBlockTagsProvider extends BlockTagsProvider {
    public UnshatteredBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, MODID);
    }

    public static final TagKey<Block> COLLECTABLE_MINING_BLOCKS = BlockTags.create(Identifier.fromNamespaceAndPath(MODID, "collectable_mining_blocks"));
    public static final TagKey<Block> COLLECTABLE_FARMING_BLOCKS = BlockTags.create(Identifier.fromNamespaceAndPath(MODID, "collectable_farming_blocks"));
    public static final TagKey<Block> COLLECTABLE_FORAGING_BLOCKS = BlockTags.create(Identifier.fromNamespaceAndPath(MODID, "collectable_foraging_blocks"));

    @Override
    protected void addTags(HolderLookup.@NonNull Provider provider) {
        tag(BlockTags.MINEABLE_WITH_AXE).add(BREAKABLE_FIG_LOG_BLOCK.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(BREAKABLE_STONE_BLOCK.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(BREAKABLE_COBBLESTONE_BLOCK.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(BREAKABLE_COAL_ORE_BLOCK.get());
        tag(BlockTags.LOGS).add(FIG_LOG_BLOCK.get());
        tag(BlockTags.LOGS).add(BREAKABLE_FIG_LOG_BLOCK.get());
        tag(COLLECTABLE_MINING_BLOCKS).add(BREAKABLE_STONE_BLOCK.get());
        tag(COLLECTABLE_MINING_BLOCKS).add(BREAKABLE_COBBLESTONE_BLOCK.get());
        tag(COLLECTABLE_MINING_BLOCKS).add(BREAKABLE_COAL_ORE_BLOCK.get());
        tag(COLLECTABLE_FORAGING_BLOCKS).add(BREAKABLE_FIG_LOG_BLOCK.get());
        tag(COLLECTABLE_FARMING_BLOCKS).add(BREAKABLE_WHEAT_BLOCK.get());

    }
}
