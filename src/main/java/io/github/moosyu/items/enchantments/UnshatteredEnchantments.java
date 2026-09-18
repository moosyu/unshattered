package io.github.moosyu.items.enchantments;

import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;

public class UnshatteredEnchantments {
    public static ResourceKey<Enchantment> RAINBOW = ResourceKey.create(Registries.ENCHANTMENT, UnshatteredUtils.getUnshatteredIdentifier("rainbow"));
}
