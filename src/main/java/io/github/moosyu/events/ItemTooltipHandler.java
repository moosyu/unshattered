package io.github.moosyu.events;

import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.components.ItemCharges;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.enchantments.UnshatteredEnchantmentEffects;
import io.github.moosyu.items.ItemTypes;
import io.github.moosyu.rarities.UnshatteredRarities;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.util.UnshatteredUtils;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class ItemTooltipHandler {
    final static int MAX_WIDTH = 200;
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        if (event.getEntity() != null && !event.getEntity().level().isClientSide()) return;
        Player player = event.getEntity();
        if (player == null) return;
        ItemStack stack = event.getItemStack();
        List<Component> tooltipComponents = event.getToolTip();
        UnshatteredRarities itemRarity = stack.getOrDefault(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.COMMON);
        ItemTypes itemType = stack.getOrDefault(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.ITEM);
        boolean hasAttributes = false;
        boolean itemDescription = Boolean.TRUE.equals(stack.get(UnshatteredDataComponents.DESCRIPTION.get()));
        ItemAbility itemAbility = stack.get(UnshatteredDataComponents.ABILITY);
        ItemCharges itemCharges = stack.get(UnshatteredDataComponents.CHARGES);
        int sellPrice = stack.getOrDefault(UnshatteredDataComponents.SELL_VALUE, 0) * stack.count();
        ItemAttributeModifiers modifiers = stack.getAttributeModifiers();
        ItemEnchantments itemEnchantments = stack.get(DataComponents.STORED_ENCHANTMENTS);

        event.getToolTip().clear();
        tooltipComponents.add(Component.translatable(stack.getItemName().getString()).withColor(itemRarity.getColour(1.0f)));

        for (ItemAttributeModifiers.Entry entry : modifiers.modifiers()) {
            Holder<Attribute> attributeHolder = entry.attribute();
            double amount = entry.modifier().amount();
            UnshatteredAttributeValues itemAttribute = UnshatteredAttributeValues.fromAttribute(attributeHolder.value());
            if (itemAttribute == null) {
                // ill make something less ugly if i end up using more vanilla attributes but for now this is fine
                if (attributeHolder == Attributes.MOVEMENT_SPEED) {
                    hasAttributes = true;
                    addAttributeToTooltip(tooltipComponents, attributeHolder, amount * 10, false, false, false);
                } else if (attributeHolder == Attributes.ENTITY_INTERACTION_RANGE) {
                    hasAttributes = true;
                    addAttributeToTooltip(tooltipComponents, attributeHolder, amount, false, false, true);
                }
            } else {
                hasAttributes = true;
                addAttributeToTooltip(tooltipComponents, attributeHolder, amount, itemAttribute.percentage, itemAttribute.offensive, true);
            }
        }

        if (itemDescription) {
            if (hasAttributes) tooltipComponents.add(Component.empty());
            addWrappedText(tooltipComponents, UnshatteredUtils.parseStyledText(Component.translatable("item.description.unshattered." + BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath()).getString(), 0xFFAAAAAA), MAX_WIDTH);
        }

        // for enchanted books
        if (itemEnchantments != null) {
            for (Map.Entry<Holder<Enchantment>, Integer> entry : itemEnchantments.entrySet()) {
                Optional<ResourceKey<Enchantment>> key = entry.getKey().unwrapKey();
                if (key.isEmpty()) continue;

                Optional<UnshatteredEnchantmentEffects.UnshatteredEffect<?>> effect = UnshatteredEnchantmentEffects.getEffect(key.get());
                if (effect.isEmpty()) continue;

                int level = entry.getValue();

                tooltipComponents.add(entry.getKey().value().description().copy().withColor(0xFF459BFF).append(Component.literal(" " + UnshatteredUtils.convertTextToRomanNumeral(level))));
                tooltipComponents.add(effect.get().getEffectDescription(level));
            }
        } else {
            List<Component> enchantmentComponents = new ArrayList<>();

            for (Object2IntMap.Entry<Holder<Enchantment>> enchantmentHolder : stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY).entrySet()) {
                Optional<ResourceKey<Enchantment>> key = enchantmentHolder.getKey().unwrapKey();
                if (key.isEmpty()) continue;
                Optional<UnshatteredEnchantmentEffects.UnshatteredEffect<?>> effect = UnshatteredEnchantmentEffects.getEffect(key.get());
                if (effect.isEmpty()) continue;

                int level = enchantmentHolder.getIntValue();
                enchantmentComponents.add(enchantmentHolder.getKey().value().description().copy().withColor((enchantmentHolder.getKey().value().getMaxLevel() == level ? 0xFFE6B605 : 0xFF459BFF)).append(Component.literal(" " + UnshatteredUtils.convertTextToRomanNumeral(level))));
            }

            MutableComponent enchantments = Component.empty();
            for (int i = 0; i < enchantmentComponents.size(); i++) {
                enchantments.append(enchantmentComponents.get(i));
                if (i < enchantmentComponents.size() - 1) {
                    enchantments.append(Component.literal(", ").withColor(0xFF459BFF));
                }
            }

            if (!enchantmentComponents.isEmpty()) {
                tooltipComponents.add(Component.empty());
                tooltipComponents.add(enchantments);
            }
        }

        if (itemAbility != null) {
            tooltipComponents.add(Component.empty());
            if (itemAbility.passive()) {
                tooltipComponents.add(Component.literal("Ability: ").append(Component.translatable("item.ability.unshattered." + itemAbility.abilityId().getPath())).withColor(0xFFFFAA00));
                addWrappedText(tooltipComponents, UnshatteredUtils.parseStyledText(Component.translatable("item.ability.description.unshattered." + itemAbility.abilityId().getPath()).getString(), 0xFFAAAAAA), MAX_WIDTH);
            } else {
                tooltipComponents.add(Component.literal("Ability: ").append(Component.translatable("item.ability.unshattered." + itemAbility.abilityId().getPath())).withColor(0xFFFFAA00).append(Component.literal(" RIGHT CLICK").withColor(0xFFFFFF55).withStyle(ChatFormatting.BOLD)));
                addWrappedText(tooltipComponents, UnshatteredUtils.parseStyledText(Component.translatable("item.ability.description.unshattered." + itemAbility.abilityId().getPath()).getString(), 0xFFAAAAAA), MAX_WIDTH);

                if (itemAbility.manaCost() > 0) tooltipComponents.add(Component.literal("Mana Cost: ").withColor(0xFF555555).append(Component.literal(String.valueOf(itemAbility.manaCost())).withColor(0xFF00AAAA)));
                if (itemAbility.cooldown() > 0) tooltipComponents.add(Component.literal("Cooldown: ").withColor(0xFF555555).append(Component.literal(String.format("%.1f", (float) itemAbility.cooldown() / 20 /* convert ticks to seconds */)).append("s").withColor(0xFF55FF55)));
                if (itemCharges != null) tooltipComponents.add(Component.literal("Charges: ").withColor(0xFF555555).append(Component.literal(String.valueOf(itemCharges.currentCharges())).withColor(0xFFFFFF55)).append(Component.literal("/").withColor(0xFF555555)).append(Component.literal((itemCharges.rechargeTime() / 20) + "s").withColor(0xFF55FF55)));
            }
        }

        tooltipComponents.add(Component.empty());

        if (sellPrice > 0) {
            tooltipComponents.add(Component.translatable("tooltip.unshattered.sell_price").withColor(0xFFAAAAAA)
                    .append(Component.literal(" "))
                    .append(Component.literal(String.format("%,d", sellPrice)).withColor(0xFFF9A604))
            );
        }

        if (itemType.reforgeable()) tooltipComponents.add(Component.translatable("tooltip.unshattered.reforgable").withColor(0xFF555555));

        tooltipComponents.add(Component.literal(Component.translatable("rarity.unshattered." + itemRarity.name().toLowerCase()).getString().toUpperCase() + " " + Component.translatable("item_type.unshattered." + itemType.getSerializedName()).getString().toUpperCase()).withColor(itemRarity.getColour(1.0f)).withStyle(ChatFormatting.BOLD));
    }

    /**
     *
     * @param tooltip tooltip for wrapped text to be added back to
     * @param text text component
     * @param maxWidth width before wrap
     */
    private static void addWrappedText(List<Component> tooltip, Component text, int maxWidth) {
        Font font = Minecraft.getInstance().font;
        List<FormattedText> lines = font.getSplitter().splitLines(text, maxWidth, text.getStyle());

        for (FormattedText line : lines) {
            MutableComponent lineComponent = Component.empty();

            line.visit((style, string) -> {
                lineComponent.append(Component.literal(string).withStyle(style));
                return Optional.empty();
            }, text.getStyle());

            tooltip.add(lineComponent);
        }
    }

    private static void addAttributeToTooltip(List<Component> tooltipComponents, Holder<Attribute> attributeHolder, double amount, boolean percentage, boolean offensive, boolean truncate) {
        tooltipComponents.add(Component.translatable(attributeHolder.value().getDescriptionId())
                .append(Component.literal(": ")).withStyle(ChatFormatting.GRAY)
                .append(Component.literal("+" + (truncate ? (int) amount : UnshatteredUtils.oneDecimalFormat.format(amount)) + (percentage ? "%" : ""))
                        .withStyle((offensive) ? ChatFormatting.RED : ChatFormatting.GREEN))
        );
    }
}
