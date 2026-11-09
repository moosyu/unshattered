package io.github.moosyu.events;

import io.github.moosyu.abilities.*;
import io.github.moosyu.abilities.data.Incremental;
import io.github.moosyu.attributes.UnshatteredAttributeValue;
import io.github.moosyu.data.components.*;
import io.github.moosyu.data.datagen.UnshatteredItemTagsProvider;
import io.github.moosyu.items.SackItem;
import io.github.moosyu.items.enchantments.UnshatteredEnchantmentEffects;
import io.github.moosyu.items.ItemType;
import io.github.moosyu.items.UnshatteredRarity;
import io.github.moosyu.util.UnshatteredUtils;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class ItemTooltipHandler {
    private final static int MAX_WIDTH = 200;
    private final static int SPEED_MULTIPLIER = 10;

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        Player player = event.getEntity();

        if (player == null || !event.getEntity().level().isClientSide()) {
            return;
        }

        ItemStack itemStack = event.getItemStack();
        List<Component> tooltipComponents = event.getToolTip();
        UnshatteredRarity itemRarity = UnshatteredUtils.getItemRarity(itemStack);
        ItemType itemType = UnshatteredUtils.getItemType(itemStack);
        boolean hasAttributes = false;
        boolean hasDescription = itemStack.is(UnshatteredItemTagsProvider.HAS_DESCRIPTION);
        ItemCharges itemCharges = itemStack.get(UnshatteredDataComponents.CHARGES);
        int sellPrice = UnshatteredUtils.getItemSellValue(itemStack) * itemStack.count();
        ItemAttributeModifiers modifiers = itemStack.getAttributeModifiers();
        ItemEnchantments itemEnchantments = itemStack.get(DataComponents.STORED_ENCHANTMENTS);
        ItemFuel itemFuel = itemStack.get(UnshatteredDataComponents.FUEL);
        ItemAttachments itemAttachments = itemStack.get(UnshatteredDataComponents.ITEM_ATTACHMENTS);
        int fuelAmount = UnshatteredUtils.getItemFuelAmount(itemStack) * itemStack.count();

        event.getToolTip().clear();
        tooltipComponents.add(Component.translatable(itemStack.getItemName().getString()).withColor(itemRarity.getColour()));

        Map<Holder<Attribute>, double[]> mergedAmounts = new LinkedHashMap<>();
        List<AttributeModifier> finalDamageModifiers = new ArrayList<>();

        for (ItemAttributeModifiers.Entry entry : modifiers.modifiers()) {
            Holder<Attribute> attributeHolder = entry.attribute();

            // not shown as its own line, folded into the damage line below
            if (isFinalDamageModifier(attributeHolder)) {
                finalDamageModifiers.add(entry.modifier());
                continue;
            }

            double amount = entry.modifier().amount();

            if (attributeHolder == Attributes.MOVEMENT_SPEED) {
                amount *= SPEED_MULTIPLIER;
            }

            mergedAmounts.computeIfAbsent(attributeHolder, _ -> new double[1])[0] += amount;
        }

        collectAbilityBonuses(player, itemStack, mergedAmounts, finalDamageModifiers);
        if (itemAttachments != null) {
            itemAttachments.attachments().forEach((_, attachment) -> collectAbilityBonuses(player, attachment, mergedAmounts, finalDamageModifiers));
        }

        double extraDamage = 0.0;
        if (!finalDamageModifiers.isEmpty()) {
            double displayedDamage = 0.0;
            for (Map.Entry<Holder<Attribute>, double[]> entry : mergedAmounts.entrySet()) {
                if (UnshatteredAttributeValue.fromAttribute(entry.getKey().value()) == UnshatteredAttributeValue.DAMAGE) {
                    displayedDamage = entry.getValue()[0];
                    break;
                }
            }

            double base = UnshatteredAttributeValue.FINAL_DAMAGE_MODIFIER.holder.value().getDefaultValue();
            if (base != 0) {
                extraDamage = displayedDamage * (evaluateModifiers(base, finalDamageModifiers) / base - 1);
            }
        }

        for (Map.Entry<Holder<Attribute>, double[]> entry : mergedAmounts.entrySet()) {
            Holder<Attribute> attributeHolder = entry.getKey();
            double amount = entry.getValue()[0];

            if (amount == 0.0) {
                continue;
            }

            UnshatteredAttributeValue itemAttribute = UnshatteredAttributeValue.fromAttribute(attributeHolder.value());

            hasAttributes = true;
            if (itemAttribute == null) {
                boolean isSpeed = attributeHolder == Attributes.MOVEMENT_SPEED;
                boolean isRange = attributeHolder == Attributes.ENTITY_INTERACTION_RANGE;

                if (isSpeed || isRange) {
                    addAttributeToTooltip(tooltipComponents, attributeHolder, amount, false, false, isRange, null);
                }
            } else {
                Component suffix = (itemAttribute == UnshatteredAttributeValue.DAMAGE && (int) extraDamage > 0)
                        ? Component.literal(" (+" + (int) extraDamage + ")").withStyle(ChatFormatting.GOLD)
                        : null;

                addAttributeToTooltip(tooltipComponents, attributeHolder, amount, itemAttribute.percentage, itemAttribute.offensive, true, suffix);
            }
        }

        if (fuelAmount > 0) {
            tooltipComponents.add(Component.translatable("tooltip.unshattered.fuelable_1").withStyle(ChatFormatting.GRAY)
                    .append(Component.literal(" +" + fuelAmount + " ").withStyle(ChatFormatting.DARK_GREEN))
                    .append(Component.translatable("tooltip.unshattered.fuelable_2"))
            );

            if (hasDescription) {
                tooltipComponents.add(Component.empty());
            }
        }

        if (hasDescription) {
            if (hasAttributes) tooltipComponents.add(Component.empty());

            addWrappedText(tooltipComponents, UnshatteredUtils.parseStyledText(Component.translatable("item.description.unshattered." + BuiltInRegistries.ITEM.getKey(itemStack.getItem()).getPath()).getString(), 0xFFAAAAAA, player));
        }

        if (itemStack.getItem() instanceof SackItem sackItem) {
            tooltipComponents.add(Component.empty());
            tooltipComponents.add(Component.translatable("tooltip.unshattered.capacity").withColor(UnshatteredUtils.GRAY)
                    .append(Component.literal(": "))
                    .append(Component.literal(String.format("%,d", sackItem.sackSlotCapacity) + " ").withColor(UnshatteredUtils.YELLOW))
                    .append(Component.translatable("tooltip.unshattered.capacity_item").withColor(UnshatteredUtils.YELLOW))
            );

            tooltipComponents.add(Component.empty());
            tooltipComponents.add(Component.translatable("tooltip.unshattered.right_click_sack").withColor(UnshatteredUtils.YELLOW));
        }

        // for enchanted books
        if (itemEnchantments != null) {
            tooltipComponents.add(Component.translatable("tooltip.unshattered.combinable").withStyle(ChatFormatting.DARK_GRAY));
            tooltipComponents.add(Component.empty());
            for (Map.Entry<Holder<Enchantment>, Integer> entry : itemEnchantments.entrySet()) {
                Optional<ResourceKey<Enchantment>> key = entry.getKey().unwrapKey();
                if (key.isEmpty()) {
                    continue;
                }

                Optional<UnshatteredEnchantmentEffects.UnshatteredSimpleEffect> effect = UnshatteredEnchantmentEffects.getEffect(key.get());
                if (effect.isEmpty()) {
                    continue;
                }

                int level = entry.getValue();

                tooltipComponents.add(entry.getKey().value().description().copy().withColor(0xFF459BFF).append(Component.literal(" " + UnshatteredUtils.convertTextToRomanNumeral(level))));
                tooltipComponents.add(effect.get().getEffectDescription(level));

                HolderSet<Item> supported = entry.getKey().value().definition().supportedItems();
                Component supportedItems = supported.unwrapKey().map(itemKey -> Component.translatable(itemKey.location().toLanguageKey()).withColor(0xFF5555FF)).orElseGet(() -> {
                    MutableComponent itemsList = Component.empty();
                    for (Holder<Item> item : supported) {
                        itemsList.append(Component.translatable(item.value().getDescriptionId()).withColor(0xFF5555FF));
                    }

                    return itemsList;
                });

                if (!supportedItems.equals(Component.empty())) {
                    tooltipComponents.add(Component.empty());
                    tooltipComponents.add(Component.translatable("tooltip.unshattered.applicable").withStyle(ChatFormatting.GRAY).append(supportedItems));
                }
            }
        } else {
            Iterator<Object2IntMap.Entry<Holder<Enchantment>>> iterator = itemStack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY).entrySet().iterator();

            if ((iterator.hasNext())) {
                MutableComponent enchantmentComponent = Component.empty();
                if (hasAttributes) {
                    tooltipComponents.add(Component.empty());
                }

                while (iterator.hasNext()) {
                    Object2IntMap.Entry<Holder<Enchantment>> enchantmentHolder = iterator.next();
                    Optional<ResourceKey<Enchantment>> key = enchantmentHolder.getKey().unwrapKey();
                    if (key.isEmpty()) {
                        continue;
                    }

                    Optional<UnshatteredEnchantmentEffects.UnshatteredSimpleEffect> effect = UnshatteredEnchantmentEffects.getEffect(key.get());
                    if (effect.isEmpty()) {
                        continue;
                    }

                    int level = enchantmentHolder.getIntValue();
                    int maxLevel = enchantmentHolder.getKey().value().getMaxLevel();
                    enchantmentComponent.append(enchantmentHolder.getKey().value().description().copy().withColor((maxLevel == level ? 0xFFE6B605 : 0xFF459BFF)).append(Component.literal((maxLevel > 1 ? " " + UnshatteredUtils.convertTextToRomanNumeral(level) : ""))));

                    if (iterator.hasNext()) {
                        enchantmentComponent.append(Component.literal(", ").withColor(0xFF459BFF));
                    }
                }

                tooltipComponents.add(enchantmentComponent);
            }

        }

        if (itemStack.getItem() instanceof AbilityItem abilityItem) {
            for (Supplier<? extends Ability> abilitySupplier : abilityItem.abilities()) {
                Ability itemAbility = abilitySupplier.get();
                tooltipComponents.add(Component.empty());
                if (itemAbility.data.passive()) {
                    tooltipComponents.add(Component.literal("Ability: ").append(Component.translatable("item.ability.unshattered." + itemAbility.id().getPath())).withColor(0xFFFFAA00));
                    addWrappedText(tooltipComponents, UnshatteredUtils.parseStyledText(Component.translatable("item.ability.description.unshattered." + itemAbility.id().getPath()).getString(), 0xFFAAAAAA, player));
                } else {
                    tooltipComponents.add(Component.literal("Ability: ").append(Component.translatable("item.ability.unshattered." + itemAbility.id().getPath())).withColor(0xFFFFAA00).append(Component.literal(" RIGHT CLICK").withColor(0xFFFFFF55).withStyle(ChatFormatting.BOLD)));
                    addWrappedText(tooltipComponents, UnshatteredUtils.parseStyledText(Component.translatable("item.ability.description.unshattered." + itemAbility.id().getPath()).getString(), 0xFFAAAAAA, player));

                    if (itemAbility.data.manaCost() > 0) tooltipComponents.add(Component.literal("Mana Cost: ").withStyle(ChatFormatting.DARK_GRAY).append(Component.literal(String.valueOf(itemAbility.data.manaCost())).withColor(0xFF00AAAA)));
                    if (itemAbility.data.cooldown() > 0) tooltipComponents.add(Component.literal("Cooldown: ").withStyle(ChatFormatting.DARK_GRAY).append(Component.literal(String.format("%.1f", (float) itemAbility.data.cooldown() / 20 /* convert ticks to seconds */)).append("s").withColor(0xFF55FF55)));
                    if (itemCharges != null) tooltipComponents.add(Component.literal("Charges: ").withStyle(ChatFormatting.DARK_GRAY).append(Component.literal(String.valueOf(itemCharges.currentCharges())).withColor(0xFFFFFF55)).append(Component.literal("/").withStyle(ChatFormatting.DARK_GRAY)).append(Component.literal((itemCharges.rechargeTime() / 20) + "s").withColor(0xFF55FF55)));
                }

                if (itemAbility instanceof Incremental incrementalAbility) {
                    tooltipComponents.add(Component.literal("(Max "
                            + incrementalAbility.attributes().stream().map(incremental -> "+"
                                    + (incrementalAbility.maxMilestone() * incremental.amountPerMilestone())
                                    + UnshatteredAttributeValue.fromAttribute(incremental.attributeHolder().value()).symbol)
                            .collect(Collectors.joining(", "))
                            + ")"
                    ).withStyle(ChatFormatting.DARK_GRAY));
                    tooltipComponents.add(Component.empty());
                    tooltipComponents.add(Component.translatable(incrementalAbility.incrementNameKey()).withStyle(ChatFormatting.GRAY)
                            .append(Component.literal(": "))
                            .append(Component.literal(String.valueOf(itemStack.getOrDefault(UnshatteredDataComponents.INCREMENTS_STORED.get(), 0))).withColor(0xFF55FF55))
                    );
                }
            }
        }

        tooltipComponents.add(Component.empty());

        if (itemAttachments != null) {
            itemAttachments.attachments().forEach(((slotType, attachmentItem) -> {
                if (attachmentItem.getItem() instanceof AbilityItem abilityItem) {
                    abilityItem.abilities().forEach(ability -> {
                        tooltipComponents.add(Component.translatable("item_type.unshattered." + slotType.correspondingType.getSerializedName()).withColor(0xFF459BFF)
                                .append(Component.literal(" "))
                                .append(attachmentItem == ItemStack.EMPTY
                                        ? Component.translatable("tooltip.unshattered.none").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.BOLD)
                                        : attachmentItem.getItemName().copy().withStyle(ChatFormatting.GREEN)
                                )
                        );

                        addWrappedText(tooltipComponents, UnshatteredUtils.parseStyledText(Component.translatable("item.ability.description.unshattered." + ability.get().id().getPath()).getString(), 0xFFAAAAAA, player));
                        tooltipComponents.add(Component.empty());
                    });
                } else {
                    tooltipComponents.add(Component.translatable("item_type.unshattered." + slotType.correspondingType.getSerializedName()).withColor(0xFF459BFF)
                            .append(Component.literal(" "))
                            .append(Component.translatable("tooltip.unshattered.none").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.BOLD))
                    );
                    tooltipComponents.add(Component.empty());
                }
            }));
        }

        if (itemFuel != null) {
            tooltipComponents.add(Component.translatable("tooltip.unshattered.fuel").withStyle(ChatFormatting.GRAY)
                    .append(Component.literal(": "))
                    .append(Component.literal(String.format("%,d", itemFuel.currentFuel())).withColor(0xFF00AA00))
                    .append(Component.literal("/" + String.format("%,d", itemFuel.getMaxFuel(itemStack))).withStyle(ChatFormatting.DARK_GRAY))
            );
            tooltipComponents.add(Component.empty());
        }

        if (sellPrice > 0) {
            tooltipComponents.add(Component.translatable("tooltip.unshattered.sell_price").withStyle(ChatFormatting.GRAY)
                    .append(Component.literal(" "))
                    .append(Component.literal(String.format("%,d", sellPrice)).withColor(0xFFF9A604))
            );
        }

        if (itemType.reforgeable()) {
            tooltipComponents.add(Component.translatable("tooltip.unshattered.reforgable").withStyle(ChatFormatting.DARK_GRAY));
        }

        boolean isAttachment = false;
        for (ItemAttachments.SlotType slotType : ItemAttachments.SlotType.values()) {
            if (slotType.correspondingType == itemType) {
                isAttachment = true;
                break;
            }
        }

        if (isAttachment) {
            tooltipComponents.add(Component.translatable("tooltip.unshattered.attachable").withStyle(ChatFormatting.DARK_GRAY));
        }

        tooltipComponents.add(Component.literal(Component.translatable("rarity.unshattered." + itemRarity.name().toLowerCase()).getString().toUpperCase() + " " + Component.translatable("item_type.unshattered." + itemType.getSerializedName()).getString().toUpperCase()).withColor(itemRarity.getColour()).withStyle(ChatFormatting.BOLD));
    }

    /**
     *
     * @param tooltip tooltip for wrapped text to be added back to
     * @param text text component
     */
    private static void addWrappedText(List<Component> tooltip, Component text) {
        Font font = Minecraft.getInstance().font;
        List<FormattedText> lines = font.getSplitter().splitLines(text, MAX_WIDTH, text.getStyle());

        for (FormattedText line : lines) {
            MutableComponent lineComponent = Component.empty();

            line.visit((style, string) -> {
                lineComponent.append(Component.literal(string).withStyle(style));
                return Optional.empty();
            }, text.getStyle());

            tooltip.add(lineComponent);
        }
    }

    private static void addAttributeToTooltip(List<Component> tooltipComponents, Holder<Attribute> attributeHolder, double amount, boolean percentage, boolean offensive, boolean truncate, @Nullable Component suffix) {
        MutableComponent line = Component.translatable(attributeHolder.value().getDescriptionId())
                .append(Component.literal(": ")).withStyle(ChatFormatting.GRAY)
                .append(Component.literal("+" + (truncate ? (int) amount : UnshatteredUtils.oneDecimalFormat.format(amount)) + (percentage ? "%" : ""))
                        .withStyle((offensive) ? ChatFormatting.RED : ChatFormatting.GREEN));

        if (suffix != null) {
            line.append(suffix);
        }

        tooltipComponents.add(line);
    }

    private static boolean isFinalDamageModifier(Holder<Attribute> holder) {
        return UnshatteredAttributeValue.fromAttribute(holder.value()) == UnshatteredAttributeValue.FINAL_DAMAGE_MODIFIER;
    }

    /**
     * does the same maths vanilla uses to turn modifiers into an attribute value:
     * (base + all ADD_VALUE) * (1 + all ADD_MULTIPLIED_BASE) * each (1 + ADD_MULTIPLIED_TOTAL)
     */
    private static double evaluateModifiers(double base, List<AttributeModifier> modifiers) {
        double add = 0.0;
        double multipliedBase = 0.0;
        double multipliedTotal = 1.0;

        for (AttributeModifier modifier : modifiers) {
            switch (modifier.operation()) {
                case ADD_VALUE -> add += modifier.amount();
                case ADD_MULTIPLIED_BASE -> multipliedBase += modifier.amount();
                case ADD_MULTIPLIED_TOTAL -> multipliedTotal *= 1 + modifier.amount();
            }
        }

        return (base + add) * (1 + multipliedBase) * multipliedTotal;
    }

    private static void collectAbilityBonuses(Player player, ItemStack source, Map<Holder<Attribute>, double[]> merged, List<AttributeModifier> finalDamageModifiers) {
        if (!(source.getItem() instanceof AbilityItem abilityItem)) {
            return;
        }

        for (Supplier<? extends Ability> supplier : abilityItem.abilities()) {
            Ability ability = supplier.get();

            if (ability.trigger != AbilityTriggerType.ONGOING) {
                continue;
            }

            AbilityContext context = new AbilityContext()
                    .add(AbilityContextKey.PLAYER, player)
                    .add(AbilityContextKey.ITEM_STACK, source);

            if (!ability.conditionsMet(context)) {
                continue;
            }

            for (UnshatteredUtils.AttributeGroup group : ability.attributeBonuses(context)) {
                AttributeModifier modifier = group.modifier();

                if (isFinalDamageModifier(group.attributeHolder())) {
                    finalDamageModifiers.add(modifier);
                    continue;
                }

                if (modifier.operation() != AttributeModifier.Operation.ADD_VALUE) {
                    continue;
                }

                double amount = modifier.amount();
                if (group.attributeHolder() == Attributes.MOVEMENT_SPEED) {
                    amount *= SPEED_MULTIPLIER;
                }

                merged.computeIfAbsent(group.attributeHolder(), _ -> new double[1])[0] += amount;
            }
        }
    }
}