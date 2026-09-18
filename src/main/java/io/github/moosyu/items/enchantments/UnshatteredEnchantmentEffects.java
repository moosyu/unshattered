package io.github.moosyu.items.enchantments;

import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
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
    /**
     * effect that's always active when applied to an item and has a single level
     */
    interface UnshatteredSimpleEffect {
        Component getEffectDescription(int level);
        /**
         * @return carpentry experience for the enchantment at level 1
         */
        float initialCarpentryExperience();

        default float getCarpentryExperience(int level) {
            return (float) Math.pow(2, level - 1) * initialCarpentryExperience();
        }
    }

    /**
     *
     * @param <T> effect context
     */
    interface UnshatteredComplexEffect<T> extends UnshatteredSimpleEffect {
        boolean checkPassesEffectRequirement(Player player, T context);
        double getEffectBonus(int level);
    }

    interface AttributeModificationComplexEffect<T> extends UnshatteredComplexEffect<T> {
        Holder<Attribute> getAttributeHolder();
        AttributeModifier getAttributeModifier(int level);
        EquipmentSlotGroup getEquipmentSlotGroup();
    }

    interface DamageComplexEffect extends UnshatteredComplexEffect<LivingEntity> {}
    interface BlockBreakingAttributeComplexEffect extends AttributeModificationComplexEffect<BlockState> {}

    Map<ResourceKey<Enchantment>, UnshatteredSimpleEffect> EFFECTS = Map.ofEntries(
            Map.entry(Enchantments.BANE_OF_ARTHROPODS, damageEffect(
                    target -> target.is(EntityTypeTags.SENSITIVE_TO_BANE_OF_ARTHROPODS),
                    level -> level * 0.15,
                    (_, _) -> "Increases damage to arthropods by: ",
                    1843.2f
            )),
            Map.entry(Enchantments.SHARPNESS, damageEffect(
                    _ -> true,
                    level -> level * 0.05,
                    (_, _) -> "Increases damage dealt by: ",
                    4096.0f
            )),
            Map.entry(Enchantments.SMITE, damageEffect(
                    target -> target.is(EntityTypeTags.SENSITIVE_TO_SMITE),
                    level -> level * 0.1,
                    (_, _) -> "Increases damage dealt to undead mobs by: ",
                    1024.0f
            )),
            Map.entry(Enchantments.EFFICIENCY, new BlockBreakingAttributeComplexEffect() {
                @Override
                public Holder<Attribute> getAttributeHolder() {
                    return UnshatteredAttributeValues.MINING_SPEED.holder;
                }

                @Override
                public AttributeModifier getAttributeModifier(int level) {
                    return new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("efficiency_mining_speed"), getEffectBonus(level), AttributeModifier.Operation.ADD_VALUE);
                }

                @Override
                public EquipmentSlotGroup getEquipmentSlotGroup() {
                    return EquipmentSlotGroup.MAINHAND;
                }

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

                @Override
                public float initialCarpentryExperience() {
                    return 1024.0f;
                }
            }),
            Map.entry(UnshatteredEnchantments.RAINBOW, new UnshatteredSimpleEffect() {
                @Override
                public Component getEffectDescription(int level) {
                    return Component.literal("Causes sheared sheep to drop ").withColor(0xFFAAAAAA)
                            .append(Component.literal("c").withColor(0xFFF5400))
                            .append(Component.literal("o").withColor(0xFFFF8E00))
                            .append(Component.literal("l").withColor(0xFFFFD200))
                            .append(Component.literal("o").withColor(0xFF81E650))
                            .append(Component.literal("u").withColor(0xFF00D267))
                            .append(Component.literal("r").withColor(0xFF00C0FF))
                            .append(Component.literal("f").withColor(0xFF8B48FE))
                            .append(Component.literal("u").withColor(0xFFCA41FC))
                            .append(Component.literal("l").withColor(0xFFFF46FB))
                            .append(Component.literal(" wool.").withColor(0xFFAAAAAA));
                }

                @Override
                public float initialCarpentryExperience() {
                    return 0;
                }
            })
    );

    static Optional<UnshatteredSimpleEffect> getEffect(ResourceKey<Enchantment> key) {
        return Optional.ofNullable(EFFECTS.get(key));
    }

    private static DamageComplexEffect damageEffect(Predicate<LivingEntity> requirement, IntToDoubleFunction bonus, BiFunction<Integer, Double, String> prefix, float carpentryExperience) {
        return new DamageComplexEffect() {
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

            @Override
            public float initialCarpentryExperience() {
                return carpentryExperience;
            }
        };
    }
}