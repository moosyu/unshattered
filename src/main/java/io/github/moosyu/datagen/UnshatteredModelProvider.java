package io.github.moosyu.datagen;

import io.github.moosyu.registers.BlocksRegistry;
import io.github.moosyu.registers.ItemsRegistry;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import static io.github.moosyu.Unshattered.MODID;

public class UnshatteredModelProvider extends ModelProvider {
    public UnshatteredModelProvider(PackOutput output) {
        super(output, MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        itemModels.generateFlatItem(ItemsRegistry.MERCENARY_AXE.get(), Items.IRON_AXE, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsRegistry.WOODEN_SWORD.get(), Items.WOODEN_SWORD, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ItemsRegistry.STONE_SWORD.get(), Items.STONE_SWORD, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ItemsRegistry.IRON_SWORD.get(), Items.IRON_SWORD, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ItemsRegistry.GOLDEN_SWORD.get(), Items.GOLDEN_SWORD, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ItemsRegistry.DIAMOND_SWORD.get(), Items.DIAMOND_SWORD, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ItemsRegistry.WOODEN_AXE.get(), Items.WOODEN_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ItemsRegistry.STONE_AXE.get(), Items.STONE_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ItemsRegistry.IRON_AXE.get(), Items.IRON_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ItemsRegistry.GOLDEN_AXE.get(), Items.GOLDEN_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ItemsRegistry.DIAMOND_AXE.get(), Items.DIAMOND_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ItemsRegistry.TREECAPITATOR.get(), Items.GOLDEN_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ItemsRegistry.SPRUCE_AXE.get(), Items.WOODEN_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ItemsRegistry.SERIOUSLY_DAMAGED_AXE.get(), Items.WOODEN_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ItemsRegistry.DECENT_AXE.get(), Items.WOODEN_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ItemsRegistry.FIG_HEW.get(), Items.WOODEN_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ItemsRegistry.FIGSTONE_SPLITTER.get(), Items.STONE_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ItemsRegistry.LEAFLET_HELMET.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsRegistry.LEAFLET_CHESTPLATE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsRegistry.LEAFLET_LEGGINGS.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsRegistry.LEAFLET_BOOTS.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsRegistry.BAT_THE_FISH.get(), Items.COOKED_COD, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsRegistry.CENTURY_THE_FISH.get(), Items.TROPICAL_FISH, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsRegistry.CHILL_THE_FISH.get(), Items.COOKED_COD, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsRegistry.CLUNK_THE_FISH.get(), Items.COD, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsRegistry.DIAMOND_THE_FISH.get(), Items.COOKED_SALMON, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsRegistry.DUST_THE_FISH.get(), Items.COOKED_SALMON, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsRegistry.EGG_THE_FISH.get(), Items.COOKED_SALMON, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsRegistry.EON_THE_FISH.get(), Items.COD, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsRegistry.FLAKE_THE_FISH.get(), Items.COOKED_SALMON, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsRegistry.EXPERIMENT_THE_FISH.get(), Items.PUFFERFISH, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsRegistry.FOSSIL_THE_FISH.get(), Items.COOKED_SALMON, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsRegistry.GABAGOOL_THE_FISH.get(), Items.SALMON, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsRegistry.GIFT_THE_FISH.get(), Items.PUFFERFISH, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsRegistry.HERRING_THE_FISH.get(), Items.SALMON, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsRegistry.NOPE_THE_FISH.get(), Items.SALMON, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsRegistry.OOPS_THE_FISH.get(), Items.TROPICAL_FISH, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsRegistry.PARTY_THE_FISH.get(), Items.TROPICAL_FISH, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsRegistry.ROCK_THE_FISH.get(), Items.COD, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsRegistry.SHRIMP_THE_FISH.get(), Items.COD, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsRegistry.SKELETON_THE_FISH.get(), Items.COOKED_COD, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsRegistry.SPOOK_THE_FISH.get(), Items.SALMON, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsRegistry.STEW_THE_FISH.get(), Items.COD, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsRegistry.SWAMP_THE_FISH.get(), Items.COOKED_SALMON, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsRegistry.ZOOP_THE_FISH.get(), Items.SALMON, ModelTemplates.FLAT_ITEM);
        // ill figure out how to fake blocks at some point
        itemModels.generateFlatItem(ItemsRegistry.ENCHANTED_FIG_LOG.get(), Items.STRIPPED_SPRUCE_LOG, ModelTemplates.FLAT_ITEM);

        blockModels.createTrivialCube(BlocksRegistry.FIG_LOG_BLOCK.get());
    }
}
