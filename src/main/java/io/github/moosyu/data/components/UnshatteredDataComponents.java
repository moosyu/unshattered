package io.github.moosyu.data.components;

import com.mojang.serialization.Codec;
import io.github.moosyu.items.ItemType;
import io.github.moosyu.items.UnshatteredRarity;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static io.github.moosyu.Unshattered.MODID;

public final class UnshatteredDataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, MODID);

    public static final Supplier<DataComponentType<UnshatteredRarity>> RARITY = DATA_COMPONENTS.registerComponentType("rarity", builder -> builder.persistent(UnshatteredRarity.CODEC));
    public static final Supplier<DataComponentType<ItemType>> ITEM_TYPE = DATA_COMPONENTS.registerComponentType("item_type", builder -> builder.persistent(ItemType.CODEC));
    public static final Supplier<DataComponentType<Boolean>> DESCRIPTION = DATA_COMPONENTS.registerComponentType("description", builder -> builder.persistent(Codec.BOOL));
    public static final Supplier<DataComponentType<ItemAbility>> ABILITY = DATA_COMPONENTS.registerComponentType("ability", builder -> builder.persistent(ItemAbility.CODEC));
    public static final Supplier<DataComponentType<ItemCharges>> CHARGES = DATA_COMPONENTS.registerComponentType("charges", builder -> builder.networkSynchronized(ItemCharges.STREAM_CODEC));
    public static final Supplier<DataComponentType<Integer>> SELL_VALUE = DATA_COMPONENTS.registerComponentType("sell_value", builder -> builder.persistent(Codec.INT));
    public static final Supplier<DataComponentType<Integer>> INCREMENTS_STORED = DATA_COMPONENTS.registerComponentType("increments_stored", builder -> builder.persistent(Codec.INT));
    public static final Supplier<DataComponentType<ItemFuel>> FUEL = DATA_COMPONENTS.registerComponentType("fuel", builder -> builder.persistent(ItemFuel.CODEC).networkSynchronized(ItemFuel.STREAM_CODEC));
}