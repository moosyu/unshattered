package io.github.moosyu.data.components;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static io.github.moosyu.Unshattered.MODID;

public final class UnshatteredDataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, MODID);

    public static final Supplier<DataComponentType<ItemCharges>> CHARGES = DATA_COMPONENTS.registerComponentType("charges", builder -> builder.networkSynchronized(ItemCharges.STREAM_CODEC));
    public static final Supplier<DataComponentType<Integer>> INCREMENTS_STORED = DATA_COMPONENTS.registerComponentType("increments_stored", builder -> builder.persistent(Codec.INT));
    public static final Supplier<DataComponentType<ItemFuel>> FUEL = DATA_COMPONENTS.registerComponentType("fuel", builder -> builder.persistent(ItemFuel.CODEC).networkSynchronized(ItemFuel.STREAM_CODEC));
    public static final Supplier<DataComponentType<ItemAttachments>> ITEM_ATTACHMENTS = DATA_COMPONENTS.registerComponentType("item_attachments", builder -> builder.persistent(ItemAttachments.CODEC).networkSynchronized(ItemAttachments.STREAM_CODEC));
}