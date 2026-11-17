package io.github.moosyu.data.datagen;

import io.github.moosyu.entities.UnshatteredEntities;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

import static io.github.moosyu.Unshattered.MODID;

public class UnshatteredEntityTagsProvider extends EntityTypeTagsProvider {
    public UnshatteredEntityTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, MODID);
    }

    public static final TagKey<EntityType<?>> MAGMATIC = TagKey.create(Registries.ENTITY_TYPE, UnshatteredUtils.getUnshatteredIdentifier("magmatic"));
    // for enemies that override normal damage and instead have a max "hit count" what is decremented by 1 with every hit
    public static final TagKey<EntityType<?>> HIT_COUNT_HEALTH = TagKey.create(Registries.ENTITY_TYPE, UnshatteredUtils.getUnshatteredIdentifier("hit_count_health"));


    @Override
    protected void addTags(HolderLookup.@NonNull Provider registries) {
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

        tag(EntityTypeTags.AQUATIC)
                .add(UnshatteredEntities.OASIS_RABBIT.get())
                .add(UnshatteredEntities.OASIS_SHEEP.get())
                .add(UnshatteredEntities.SEA_WALKER.get())
                .add(UnshatteredEntities.FROZEN_STEVE.get())
                .add(UnshatteredEntities.GRINCH.get());

        tag(HIT_COUNT_HEALTH)
                .add(UnshatteredEntities.GRINCH.get());
    }
}
