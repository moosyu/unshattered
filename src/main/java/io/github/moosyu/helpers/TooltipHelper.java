package io.github.moosyu.helpers;

import io.github.moosyu.rarities.RarityTypes;
import io.github.moosyu.registers.DataComponentRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.List;

public class TooltipHelper {
    public static void displayHoverText(ItemStack stack, List<Component> tooltipComponents, String itemType) {
        RarityTypes itemRarity = stack.get(DataComponentRegistry.RARITY.get());
        if (itemRarity == null) {
            itemRarity = RarityTypes.COMMON;
        }
        tooltipComponents.clear();

        tooltipComponents.add(Component.translatable(stack.getDescriptionId()).withColor(itemRarity.getColor()));
        ItemAttributeModifiers modifiers = stack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);

        for (ItemAttributeModifiers.Entry entry : modifiers.modifiers()) {
            Holder<Attribute> attributeHolder = entry.attribute();
            AttributeModifier modifier = entry.modifier();
            System.out.println(modifier.id());

            tooltipComponents.add(Component.translatable(attributeHolder.value().getDescriptionId()).append(Component.literal(": ")).withStyle(ChatFormatting.GRAY)
                    .append(Component.literal("+").withStyle(ChatFormatting.RED))
                    .append(Component.literal(String.valueOf((int) modifier.amount())).withStyle(ChatFormatting.RED))
            );
        }

        tooltipComponents.add(Component.empty());
        tooltipComponents.add(Component.literal(Component.translatable("rarity.nno." + itemRarity.name().toLowerCase()).getString().toUpperCase()).append(Component.literal(" ")).append(Component.literal(itemType.toUpperCase())).withColor(itemRarity.getColor()).withStyle(ChatFormatting.BOLD));
    }
}
