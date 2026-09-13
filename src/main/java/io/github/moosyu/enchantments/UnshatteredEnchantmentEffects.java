package io.github.moosyu.enchantments;

import io.github.moosyu.attributes.UnshatteredAttributeValues;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;
import java.util.Optional;

public interface UnshatteredEnchantmentEffects {
    interface DamageEffect extends UnshatteredEffect {
        boolean checkPassesEffectRequirement(Player player, LivingEntity target);
    }

    interface MiningSpeedEffect extends UnshatteredEffect {
        boolean checkPassesEffectRequirement(Player player, BlockState state);
    }

    interface UnshatteredEffect {
        double getEffectBonus(int level);
        Component getEffectDescription(int level);
    }

    Map<ResourceKey<Enchantment>, DamageEffect> DAMAGE_EFFECTS = Map.ofEntries(
            Map.entry(Enchantments.BANE_OF_ARTHROPODS, new DamageEffect() {
                @Override
                public boolean checkPassesEffectRequirement(Player player, LivingEntity target) {
                    return target.is(EntityTypeTags.SENSITIVE_TO_BANE_OF_ARTHROPODS);
                }

                @Override
                public double getEffectBonus(int level) {
                    return level * 0.15;
                }

                @Override
                public Component getEffectDescription(int level) {
                    return Component.literal("Increases damage to arthropods by: ").withColor(0xFF555555)
                            .append(Component.literal((getEffectBonus(level) * 100) + "%")).withColor(0xFF65EC66);
                }
            }),
            Map.entry(Enchantments.SHARPNESS, new DamageEffect() {
                @Override
                public boolean checkPassesEffectRequirement(Player player, LivingEntity target) {
                    return true;
                }

                @Override
                public double getEffectBonus(int level) {
                    return level * 0.05;
                }

                @Override
                public Component getEffectDescription(int level) {
                    return Component.literal("Increases damage dealt by: ").withColor(0xFF555555)
                            .append(Component.literal((getEffectBonus(level) * 100) + "%")).withColor(0xFF65EC66);
                }
            }),
            Map.entry(Enchantments.SMITE, new DamageEffect() {
                @Override
                public boolean checkPassesEffectRequirement(Player player, LivingEntity target) {
                    return target.is(EntityTypeTags.SENSITIVE_TO_SMITE);
                }

                @Override
                public double getEffectBonus(int level) {
                    return level * 0.1;
                }

                @Override
                public Component getEffectDescription(int level) {
                    return Component.literal("Increases damage dealt to undead mobs by: ")
                            .append(Component.literal((getEffectBonus(level) * 100) + "%")).withColor(0xFF65EC66);
                }
            })
    );

    Map<ResourceKey<Enchantment>, MiningSpeedEffect> MINING_SPEED_EFFECTS = Map.ofEntries(
            Map.entry(Enchantments.EFFICIENCY, new MiningSpeedEffect() {
                @Override
                public boolean checkPassesEffectRequirement(Player player, BlockState state) {
                    return true;
                }

                @Override
                public double getEffectBonus(int level) {
                    return level * 5.0d;
                }

                @Override
                public Component getEffectDescription(int level) {
                    return Component.literal("Grants "
                            + getEffectBonus(level)
                            + " "
                            + UnshatteredAttributeValues.MINING_SPEED.symbol
                            + " ")
                            .append(Component.translatable("attribute.name.unshattered." + UnshatteredAttributeValues.MINING_SPEED.id));
                }
            })
    );

    static Optional<UnshatteredEffect> getEffect(ResourceKey<Enchantment> key) {
        UnshatteredEnchantmentEffects.DamageEffect damageEffect = UnshatteredEnchantmentEffects.DAMAGE_EFFECTS.get(key);

        if (damageEffect != null) {
            return Optional.of(damageEffect);
        }

        UnshatteredEnchantmentEffects.MiningSpeedEffect miningEffect = UnshatteredEnchantmentEffects.MINING_SPEED_EFFECTS.get(key);

        if (miningEffect != null) {
            return Optional.of(miningEffect);
        }

        return Optional.empty();
    }
}