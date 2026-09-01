package io.github.moosyu.data.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.items.UnshatteredItems.*;

public class UnshatteredItemTagsProvider extends ItemTagsProvider {
    public UnshatteredItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, MODID);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider provider) {
        tag(ItemTags.HEAD_ARMOR).add(LEAFLET_HELMET.get());
        tag(ItemTags.CHEST_ARMOR).add(LEAFLET_CHESTPLATE.get());
        tag(ItemTags.LEG_ARMOR).add(LEAFLET_LEGGINGS.get());
        tag(ItemTags.FOOT_ARMOR).add(LEAFLET_BOOTS.get());
        tag(ItemTags.AXES).remove(Items.WOODEN_AXE);
        tag(ItemTags.AXES).remove(Items.GOLDEN_AXE);
        tag(ItemTags.AXES).remove(Items.STONE_AXE);
        tag(ItemTags.AXES).remove(Items.COPPER_AXE);
        tag(ItemTags.AXES).remove(Items.IRON_AXE);
        tag(ItemTags.AXES).remove(Items.DIAMOND_AXE);
        tag(ItemTags.AXES).remove(Items.NETHERITE_AXE);
    }
}
