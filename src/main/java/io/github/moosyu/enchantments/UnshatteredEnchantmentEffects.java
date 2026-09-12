package io.github.moosyu.enchantments;

import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public interface UnshatteredEnchantmentEffects {
    @FunctionalInterface
    interface DamageEffect {
        double getFinalDamageModifierBonus(Player player, LivingEntity target, int level);
    }

    @FunctionalInterface
    interface MiningSpeedEffect {
        double getMiningSpeedBonus(Player player, BlockState state, int level);
    }

    Map<ResourceKey<Enchantment>, DamageEffect> DAMAGE_EFFECTS = Map.ofEntries(
            Map.entry(Enchantments.BANE_OF_ARTHROPODS, (player, target, level) -> target.is(EntityTypeTags.ARTHROPOD) ? level * 0.1 : 0.0),
            Map.entry(Enchantments.SHARPNESS, (player, target, level) -> level * 0.1),
            Map.entry(Enchantments.SMITE, (player, target, level) -> target.is(EntityTypeTags.UNDEAD) ? level * 0.1 : 0.0)
    );

    Map<ResourceKey<Enchantment>, MiningSpeedEffect> MINING_SPEED_EFFECTS = Map.ofEntries(
            Map.entry(Enchantments.EFFICIENCY, (player, state, level) -> level * 5.0)
    );
}