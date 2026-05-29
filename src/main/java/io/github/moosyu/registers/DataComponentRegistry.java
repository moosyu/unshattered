package io.github.moosyu.registers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.moosyu.items.ItemTypes;
import io.github.moosyu.rarities.RarityTypes;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static io.github.moosyu.NNO.MODID;

public class DataComponentRegistry {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, MODID);

    public static final Supplier<DataComponentType<RarityTypes>> RARITY = DATA_COMPONENTS.registerComponentType("rarity", builder -> builder.persistent(RarityTypes.CODEC));
    public static final Supplier<DataComponentType<ItemTypes>> ITEM_TYPE = DATA_COMPONENTS.registerComponentType("item_type", builder -> builder.persistent(ItemTypes.CODEC));
    public static final Supplier<DataComponentType<String>> DESCRIPTION_KEY = DATA_COMPONENTS.registerComponentType("description_key", builder -> builder.persistent(Codec.STRING));
}