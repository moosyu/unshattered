package io.github.moosyu.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;

import java.util.concurrent.CompletableFuture;

import static io.github.moosyu.Unshattered.MODID;

public class UnshatteredEntityTagsProvider extends EntityTypeTagsProvider {
    public UnshatteredEntityTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        tag(EntityTypeTags.BURN_IN_DAYLIGHT)
                .remove(EntityType.SKELETON)
                .remove(EntityType.ZOMBIE)
                .remove(EntityType.ZOMBIE_VILLAGER)
                .remove(EntityType.ZOMBIE_HORSE)
                .remove(EntityType.STRAY)
                .remove(EntityType.WITHER_SKELETON)
                .remove(EntityType.BOGGED)
                .remove(EntityType.DROWNED)
                .remove(EntityType.ZOMBIE_NAUTILUS)
                .remove(EntityType.PHANTOM);
    }
}
