package io.github.moosyu.data.components;

import com.mojang.serialization.Codec;
import io.github.moosyu.items.ItemTypes;
import io.github.moosyu.rarities.RarityTypes;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static io.github.moosyu.Unshattered.MODID;

public final class DataComponentRegistry {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, MODID);

    public static final Supplier<DataComponentType<RarityTypes>> RARITY = DATA_COMPONENTS.registerComponentType("rarity", builder -> builder.persistent(RarityTypes.CODEC));
    public static final Supplier<DataComponentType<ItemTypes>> ITEM_TYPE = DATA_COMPONENTS.registerComponentType("item_type", builder -> builder.persistent(ItemTypes.CODEC));
    public static final Supplier<DataComponentType<String>> DESCRIPTION_KEY = DATA_COMPONENTS.registerComponentType("description_key", builder -> builder.persistent(Codec.STRING));
    public static final Supplier<DataComponentType<SkillRequirement>> SKILL_REQUIREMENT = DATA_COMPONENTS.registerComponentType("skill_requirement", builder -> builder.persistent(SkillRequirement.CODEC));
    public static final Supplier<DataComponentType<ToolAbility>> ITEM_ABILITY = DATA_COMPONENTS.registerComponentType("item_ability", builder -> builder.persistent(ToolAbility.CODEC));

}