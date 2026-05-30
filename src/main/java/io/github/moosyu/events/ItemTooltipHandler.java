package io.github.moosyu.events;

import io.github.moosyu.attributes.ModAttributes;
import io.github.moosyu.items.ItemTypes;
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
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.List;

import static io.github.moosyu.Unshattered.MODID;

public class ItemTooltipHandler {
    @EventBusSubscriber(modid = MODID)
    public static class EventHandler {
        @SubscribeEvent
        public static void onItemTooltip(ItemTooltipEvent event) {
            if (event.getEntity() != null && !event.getEntity().level().isClientSide()) return;
            ItemStack stack = event.getItemStack();
            List<Component> tooltipComponents = event.getToolTip();
            RarityTypes itemRarity = stack.get(DataComponentRegistry.RARITY.get());
            ItemTypes itemType = stack.get(DataComponentRegistry.ITEM_TYPE.get());
            String itemDescriptionKey = stack.get(DataComponentRegistry.DESCRIPTION_KEY.get());

            event.getToolTip().clear();
            if (itemRarity == null) itemRarity = RarityTypes.COMMON;
            if (itemType == null) itemType = ItemTypes.ITEM;

            tooltipComponents.add(Component.translatable(stack.getDescriptionId()).withColor(itemRarity.getColor()));
            ItemAttributeModifiers modifiers = stack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);

            if (itemDescriptionKey != null) {tooltipComponents.add(Component.translatable(itemDescriptionKey).withColor(0xFFAAAAAA));}

            for (ItemAttributeModifiers.Entry entry : modifiers.modifiers()) {
                Holder<Attribute> attributeHolder = entry.attribute();
                AttributeModifier modifier = entry.modifier();
                ModAttributes modAttribute = ModAttributes.fromAttribute(attributeHolder.value());

                if (modAttribute == null) continue;
                tooltipComponents.add(Component.translatable(attributeHolder.value().getDescriptionId()).append(Component.literal(": ")).withStyle(ChatFormatting.GRAY).append(Component.literal("+" + (int) modifier.amount()).withStyle((modAttribute.offensive) ? ChatFormatting.RED : ChatFormatting.GREEN)));
            }

            tooltipComponents.add(Component.empty());
            if (itemType.reforgeable()) {tooltipComponents.add(Component.translatable("tooltip.unshattered.reforgable").withColor(0xFF555555));}
            // tooltipComponents.add(Component.literal("❣ ").withColor(0xFFAA0000).append(Component.literal("Requires").withColor(0xFFFF5555)).append(Component.literal(" Combat Skill 4.").withColor(0xFF55FF55)));
            tooltipComponents.add(Component.literal(Component.translatable("rarity.unshattered." + itemRarity.name().toLowerCase()).getString().toUpperCase()+ " " + Component.translatable(itemType.getKey()).getString().toUpperCase()).withColor(itemRarity.getColor()).withStyle(ChatFormatting.BOLD));
        }
    }
}
