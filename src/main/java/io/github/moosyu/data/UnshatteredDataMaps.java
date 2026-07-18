package io.github.moosyu.data;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

import static io.github.moosyu.Unshattered.MODID;

public class UnshatteredDataMaps {
    public static final DataMapType<Block, Float> HARVESTABLE_BLOCKS_EXP_DATA = DataMapType.builder(
            Identifier.fromNamespaceAndPath(MODID, "harvestable_blocks_exp_data"),
            Registries.BLOCK,
            Codec.FLOAT
    ).build();

    public static final DataMapType<Item, Float> FISHABLE_ITEMS_EXP_DATA = DataMapType.builder(
            Identifier.fromNamespaceAndPath(MODID, "fishable_items_exp_data"),
            Registries.ITEM,
            Codec.FLOAT
    ).build();

    public static final DataMapType<EntityType<?>, Float> FISHABLE_MOBS_EXP_DATA = DataMapType.builder(
            Identifier.fromNamespaceAndPath(MODID, "fishable_mobs_exp_data"),
            Registries.ENTITY_TYPE,
            Codec.FLOAT
    ).build();

    public static final DataMapType<EntityType<?>, MobRewardData> COMBATABLE_MOBS_LOOT_DATA = DataMapType.builder(
            Identifier.fromNamespaceAndPath(MODID, "combatable_mobs_exp_data"),
            Registries.ENTITY_TYPE,
            MobRewardData.CODEC
    ).build();


}
