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
import java.util.function.BiFunction;
import java.util.function.IntToDoubleFunction;
import java.util.function.Predicate;

public interface UnshatteredEnchantmentEffects {
    interface UnshatteredEffect<T> {
        boolean checkPassesEffectRequirement(Player player, T context);
        double getEffectBonus(int level);
        Component getEffectDescription(int level);
    }

    interface DamageEffect extends UnshatteredEffect<LivingEntity> {}
    interface MiningSpeedEffect extends UnshatteredEffect<BlockState> {}

    Map<ResourceKey<Enchantment>, UnshatteredEffect<?>> EFFECTS = Map.ofEntries(
            Map.entry(Enchantments.BANE_OF_ARTHROPODS, damageEffect(
                    target -> target.is(EntityTypeTags.SENSITIVE_TO_BANE_OF_ARTHROPODS),
                    level -> level * 0.15,
                    (_, _) -> "Increases damage to arthropods by: "
            )),
            Map.entry(Enchantments.SHARPNESS, damageEffect(
                    _ -> true,
                    level -> level * 0.05,
                    (_, _) -> "Increases damage dealt by: "
            )),
            Map.entry(Enchantments.SMITE, damageEffect(
                    target -> target.is(EntityTypeTags.SENSITIVE_TO_SMITE),
                    level -> level * 0.1,
                    (_, _) -> "Increases damage dealt to undead mobs by: "
            )),
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
                    return Component.literal("Grants ").withColor(0xFFAAAAAA)
                            .append(Component.literal("+" + (int) getEffectBonus(level) + UnshatteredAttributeValues.MINING_SPEED.symbol + ".").withColor(UnshatteredAttributeValues.MINING_SPEED.color));
                }
            })
    );

    static Optional<UnshatteredEffect<?>> getEffect(ResourceKey<Enchantment> key) {
        return Optional.ofNullable(EFFECTS.get(key));
    }

    private static DamageEffect damageEffect(Predicate<LivingEntity> requirement, IntToDoubleFunction bonus, BiFunction<Integer, Double, String> prefix) {
        return new DamageEffect() {
            @Override
            public boolean checkPassesEffectRequirement(Player player, LivingEntity target) {
                return requirement.test(target);
            }

            @Override
            public double getEffectBonus(int level) {
                return bonus.applyAsDouble(level);
            }

            @Override
            public Component getEffectDescription(int level) {
                double bonus = getEffectBonus(level);
                return Component.literal(prefix.apply(level, bonus)).withColor(0xFFAAAAAA)
                        .append(Component.literal(Math.round(bonus * 100) + "%").withColor(0xFF65EC66));
            }
        };
    }
}