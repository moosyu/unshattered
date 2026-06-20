package io.github.moosyu.datagen;

import io.github.moosyu.blocks.UnshatteredBlocks;
import io.github.moosyu.items.UnshatteredItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.*;
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
        itemModels.generateFlatItem(UnshatteredItems.MERCENARY_AXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.WOODEN_SWORD.get(), Items.WOODEN_SWORD, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.STONE_SWORD.get(), Items.STONE_SWORD, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.IRON_SWORD.get(), Items.IRON_SWORD, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.GOLDEN_SWORD.get(), Items.GOLDEN_SWORD, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.DIAMOND_SWORD.get(), Items.DIAMOND_SWORD, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.WOODEN_AXE.get(), Items.WOODEN_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.STONE_AXE.get(), Items.STONE_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.IRON_AXE.get(), Items.IRON_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.ROGUE_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.GOLDEN_AXE.get(), Items.GOLDEN_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.DIAMOND_AXE.get(), Items.DIAMOND_AXE, ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.TREECAPITATOR.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.SPRUCE_AXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.SERIOUSLY_DAMAGED_AXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.DECENT_AXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.FIG_HEW.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.FIGSTONE_SPLITTER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.SQUIRE_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.UNDEAD_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.ZOMBIE_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.ORNATE_ZOMBIE_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.FLORID_ZOMBIE_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.GOLDEN_CLEAVER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.SUPER_CLEAVER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.HYPER_CLEAVER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.GIANT_CLEAVER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.RUSTY_CLEAVER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.LEAFLET_HELMET.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.LEAFLET_CHESTPLATE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.LEAFLET_LEGGINGS.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.LEAFLET_BOOTS.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.BAT_THE_FISH.get(), Items.COOKED_COD, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.CENTURY_THE_FISH.get(), Items.TROPICAL_FISH, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.CHILL_THE_FISH.get(), Items.COOKED_COD, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.CLUNK_THE_FISH.get(), Items.COD, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.DIAMOND_THE_FISH.get(), Items.COOKED_SALMON, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.DUST_THE_FISH.get(), Items.COOKED_SALMON, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.EGG_THE_FISH.get(), Items.COOKED_SALMON, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.EON_THE_FISH.get(), Items.COD, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.FLAKE_THE_FISH.get(), Items.COOKED_SALMON, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.EXPERIMENT_THE_FISH.get(), Items.PUFFERFISH, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.FOSSIL_THE_FISH.get(), Items.COOKED_SALMON, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.GABAGOOL_THE_FISH.get(), Items.SALMON, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.GIFT_THE_FISH.get(), Items.PUFFERFISH, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.HERRING_THE_FISH.get(), Items.SALMON, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.NOPE_THE_FISH.get(), Items.SALMON, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.OOPS_THE_FISH.get(), Items.TROPICAL_FISH, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.PARTY_THE_FISH.get(), Items.TROPICAL_FISH, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.ROCK_THE_FISH.get(), Items.COD, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.SHRIMP_THE_FISH.get(), Items.COD, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.SKELETON_THE_FISH.get(), Items.COOKED_COD, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.SPOOK_THE_FISH.get(), Items.SALMON, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.STEW_THE_FISH.get(), Items.COD, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.SWAMP_THE_FISH.get(), Items.COOKED_SALMON, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.ZOOP_THE_FISH.get(), Items.SALMON, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.CAKE_SOUL.get(), Items.PINK_DYE, ModelTemplates.FLAT_ITEM);
        itemModels.generateFishingRod(UnshatteredItems.CHALLENGING_ROD.get());
        itemModels.itemModelOutput.accept(UnshatteredItems.ENCHANTED_FIG_LOG.get(), ItemModelUtils.plainModel(ModelTemplates.CUBE_COLUMN.create(Identifier.fromNamespaceAndPath("unshattered", "block/enchanted_fig_log"), TextureMapping.column(UnshatteredBlocks.FIG_LOG_BLOCK.get()), blockModels.modelOutput)));
        itemModels.itemModelOutput.accept(UnshatteredItems.BEDROCK.get(), ItemModelUtils.plainModel(ModelTemplates.CUBE_ALL.create(Identifier.fromNamespaceAndPath("unshattered", "block/bedrock"), TextureMapping.cube(Blocks.BEDROCK), blockModels.modelOutput)));

        blockModels.createRotatedPillarWithHorizontalVariant(UnshatteredBlocks.FIG_LOG_BLOCK.get(), TexturedModel.COLUMN, TexturedModel.COLUMN_HORIZONTAL);
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(UnshatteredBlocks.BREAKABLE_STONE_BLOCK.get(), BlockModelGenerators.plainVariant(ModelTemplates.CUBE_ALL.create(UnshatteredBlocks.BREAKABLE_STONE_BLOCK.get(), TextureMapping.cube(Blocks.STONE), blockModels.modelOutput))));
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(UnshatteredBlocks.BREAKABLE_COBBLESTONE_BLOCK.get(), BlockModelGenerators.plainVariant(ModelTemplates.CUBE_ALL.create(UnshatteredBlocks.BREAKABLE_COBBLESTONE_BLOCK.get(), TextureMapping.cube(Blocks.COBBLESTONE), blockModels.modelOutput))));
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(UnshatteredBlocks.BREAKABLE_FIG_LOG_BLOCK.get(), BlockModelGenerators.plainVariant(ModelTemplates.CUBE_COLUMN.create(UnshatteredBlocks.BREAKABLE_FIG_LOG_BLOCK.get(), TextureMapping.column(UnshatteredBlocks.FIG_LOG_BLOCK.get()), blockModels.modelOutput))));
    }
}
