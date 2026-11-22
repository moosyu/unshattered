package io.github.moosyu.data.datagen;

import io.github.moosyu.Unshattered;
import io.github.moosyu.abilities.data.PainToPower;
import io.github.moosyu.entities.projectile.terrain.TossedTerrain;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class UnshatteredDamageTypeTagsProvider extends DamageTypeTagsProvider {
    public UnshatteredDamageTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Unshattered.MODID);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider registries) {
        tag(DamageTypeTags.NO_KNOCKBACK).add(PainToPower.BLOOD_CHALICE_DAMAGE_TYPE);
        tag(DamageTypeTags.IS_PROJECTILE).add(TossedTerrain.TERRAIN_CRUSH);
        tag(DamageTypeTags.BYPASSES_SHIELD).add(TossedTerrain.TERRAIN_CRUSH);
    }
}
