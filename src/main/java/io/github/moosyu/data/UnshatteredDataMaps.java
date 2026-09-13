package io.github.moosyu.data;

import com.mojang.serialization.Codec;
import io.github.moosyu.collectables.CollectableItemEntry;
import io.github.moosyu.data.drops.DropData;
import io.github.moosyu.data.drops.MobRewardData;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

import java.util.List;

import static io.github.moosyu.Unshattered.MODID;

public final class UnshatteredDataMaps {
    public static final DataMapType<Block, Float> HARVESTABLE_BLOCKS_EXP_DATA = DataMapType.builder(
            UnshatteredUtils.getUnshatteredIdentifier("harvestable_blocks_exp_data", false),
            Registries.BLOCK,
            Codec.FLOAT
    ).build();

    public static final DataMapType<Item, Float> FISHABLE_ITEMS_EXP_DATA = DataMapType.builder(
            UnshatteredUtils.getUnshatteredIdentifier("fishable_items_exp_data", false),
            Registries.ITEM,
            Codec.FLOAT
    ).build();

    public static final DataMapType<EntityType<?>, Float> FISHABLE_MOBS_EXP_DATA = DataMapType.builder(
            UnshatteredUtils.getUnshatteredIdentifier("fishable_mobs_exp_data", false),
            Registries.ENTITY_TYPE,
            Codec.FLOAT
    ).build();

    public static final DataMapType<EntityType<?>, MobRewardData> COMBATABLE_MOBS_LOOT_DATA = DataMapType.builder(
            UnshatteredUtils.getUnshatteredIdentifier("combatable_mobs_exp_data", false),
            Registries.ENTITY_TYPE,
            MobRewardData.CODEC
    ).build();

    public static final DataMapType<Block, Integer> BLOCK_BREAKING_POWER_DATA = DataMapType.builder(
            UnshatteredUtils.getUnshatteredIdentifier("block_breaking_power_data", false),
            Registries.BLOCK,
            Codec.INT
    ).build();

    public static final DataMapType<Block, List<DropData>> BREAKABLE_DROPS_DATA = DataMapType.builder(
            UnshatteredUtils.getUnshatteredIdentifier("breakable_drops_data", false),
            Registries.BLOCK,
            DropData.CODEC.listOf()
    ).build();

    public static final DataMapType<Item, CollectableItemEntry> COLLECTABLE_DATA = DataMapType.builder(
            UnshatteredUtils.getUnshatteredIdentifier("collectable_data", false),
            Registries.ITEM,
            CollectableItemEntry.CODEC
    ).build();
}
