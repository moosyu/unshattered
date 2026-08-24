package io.github.moosyu.data.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.blocks.UnshatteredBlocks.*;

public class UnshatteredBlockTagsProvider extends BlockTagsProvider {
    public UnshatteredBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, MODID);
    }

    public static final TagKey<Block> BREAKABLE_BLOCKS = BlockTags.create(Identifier.fromNamespaceAndPath(MODID, "breakable_blocks"));
    public static final TagKey<Block> COLLECTABLE_MINING_BLOCKS = BlockTags.create(Identifier.fromNamespaceAndPath(MODID, "collectable_mining_blocks"));
    public static final TagKey<Block> COLLECTABLE_FARMING_BLOCKS = BlockTags.create(Identifier.fromNamespaceAndPath(MODID, "collectable_farming_blocks"));
    public static final TagKey<Block> COLLECTABLE_FORAGING_BLOCKS = BlockTags.create(Identifier.fromNamespaceAndPath(MODID, "collectable_foraging_blocks"));
    public static final List<Block> BREAKABLE_BLOCKS_LIST = Stream.of(
            BREAKABLE_FIG_LOG_BLOCK,
            BREAKABLE_COBBLESTONE_BLOCK,
            BREAKABLE_STONE_BLOCK
    ).map(DeferredHolder::get).toList();

    @Override
    protected void addTags(HolderLookup.@NonNull Provider provider) {
        tag(BlockTags.MINEABLE_WITH_AXE).add(BREAKABLE_FIG_LOG_BLOCK.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(BREAKABLE_STONE_BLOCK.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(BREAKABLE_COBBLESTONE_BLOCK.get());
        tag(BlockTags.LOGS).add(FIG_LOG_BLOCK.get());
        tag(BlockTags.LOGS).add(BREAKABLE_FIG_LOG_BLOCK.get());
        tag(BREAKABLE_BLOCKS).addAll(BREAKABLE_BLOCKS_LIST);
        tag(COLLECTABLE_MINING_BLOCKS).add(BREAKABLE_STONE_BLOCK.get());
        tag(COLLECTABLE_MINING_BLOCKS).add(BREAKABLE_COBBLESTONE_BLOCK.get());
        tag(COLLECTABLE_FORAGING_BLOCKS).add(BREAKABLE_FIG_LOG_BLOCK.get());

    }
}
