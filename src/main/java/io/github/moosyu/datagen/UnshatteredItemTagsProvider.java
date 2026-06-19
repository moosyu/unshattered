package io.github.moosyu.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.items.UnshatteredItems.*;

public class UnshatteredItemTagsProvider extends ItemTagsProvider {
    public UnshatteredItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ItemTags.HEAD_ARMOR).add(LEAFLET_HELMET.get());
        tag(ItemTags.CHEST_ARMOR).add(LEAFLET_CHESTPLATE.get());
        tag(ItemTags.LEG_ARMOR).add(LEAFLET_LEGGINGS.get());
        tag(ItemTags.FOOT_ARMOR).add(LEAFLET_BOOTS.get());
    }
}
