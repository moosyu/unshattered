package io.github.moosyu.datagen;

import io.github.moosyu.blocks.UnshatteredBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.DataMapProvider;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

import static io.github.moosyu.data.UnshatteredDataMaps.*;

public class UnshatteredDataMapProvider extends DataMapProvider {
    public UnshatteredDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.@NonNull Provider provider) {
        this.builder(HARVESTABLE_BLOCKS_EXP_DATA)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_FIG_LOG_BLOCK.get()), 15.0f, false)
                .add(BlockTags.FLOWERS, 1.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.OAK_LOG), 6.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.SPRUCE_LOG), 6.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.BIRCH_LOG), 6.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.JUNGLE_LOG), 6.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.ACACIA_LOG), 6.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.DARK_OAK_LOG), 6.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.WHEAT), 4.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.POTATOES), 4.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.CARROTS), 4.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.PUMPKIN), 4.5f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.MELON), 4.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.SUGAR_CANE), 2.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.MUSHROOM_STEM), 2.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.BROWN_MUSHROOM_BLOCK), 2.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.RED_MUSHROOM_BLOCK), 2.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.BROWN_MUSHROOM), 6.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.RED_MUSHROOM), 6.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.CACTUS), 2.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.COCOA), 4.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.NETHER_WART), 4.0f, false);
        this.builder(FISHABLE_ITEMS_EXP_DATA)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.COD), 0.5f, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.SALMON), 0.7f, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.PUFFERFISH), 1.0f, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.TROPICAL_FISH), 2.0f, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.PRISMARINE_SHARD), 0.5f, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.PRISMARINE_CRYSTALS), 0.5f, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.CLAY_BALL), 0.1f, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.SPONGE), 4.0f, false);
        this.builder(FISHABLE_MOBS_EXP_DATA)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.SQUID), 25.0f, false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.GLOW_SQUID), 90.0f, false);
        this.builder(COMBATABLE_MOBS_EXP_DATA)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.ZOMBIE), 6.0f, false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.SKELETON), 6.0f, false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.SLIME), 4.0f, false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.SPIDER), 8.0f, false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.CAVE_SPIDER), 8.0f, false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.WITCH), 15.0f, false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.ENDERMAN), 15.0f, false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.BAT), 33.0f, false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.CREEPER), 8.0f, false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.BLAZE), 10.0f, false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.SQUID), 12.0f, false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.GLOW_SQUID), 36.0f, false);
        this.builder(FARMING_MOBS_EXP_DATA)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.SHEEP), 4.0f, false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.COW), 3.0f, false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.CHICKEN), 4.0f, false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.RABBIT), 4.0f, false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.PIG), 4.0f, false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.MOOSHROOM), 5.0f, false);
    }
}
