package io.github.moosyu.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.util.concurrent.CompletableFuture;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.registers.BlocksRegistry.FIG_LOG_BLOCK;

public class UnshatteredBlockTagsProvider extends BlockTagsProvider {
    public UnshatteredBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_AXE).add(FIG_LOG_BLOCK.get());
        tag(BlockTags.LOGS).add(FIG_LOG_BLOCK.get());
    }
}
