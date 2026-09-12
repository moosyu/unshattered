package io.github.moosyu.enchantments;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

import static io.github.moosyu.Unshattered.MODID;

public class UnshatteredEnchantments {
    public static final DeferredRegister<DataComponentType<?>> ENCHANTMENT_COMPONENT_TYPES = DeferredRegister.create(Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, MODID);
}
