package io.github.moosyu.events;

import io.github.moosyu.attachments.PlayerSkillsAttachment;
import io.github.moosyu.attributes.UnshatteredAttributes;
import io.github.moosyu.data.components.SkillRequirement;
import io.github.moosyu.items.ItemTypes;
import io.github.moosyu.rarities.RarityTypes;
import io.github.moosyu.attachments.AttachmentRegistry;
import io.github.moosyu.data.components.DataComponentRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class ItemTooltipHandler {
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        if (event.getEntity() != null && !event.getEntity().level().isClientSide()) return;
        Player player = event.getEntity();
        if (player == null) return;
        ItemStack stack = event.getItemStack();
        List<Component> tooltipComponents = event.getToolTip();
        RarityTypes itemRarity = stack.get(DataComponentRegistry.RARITY.get());
        ItemTypes itemType = stack.get(DataComponentRegistry.ITEM_TYPE.get());
        String itemDescriptionKey = stack.get(DataComponentRegistry.DESCRIPTION_KEY.get());
        SkillRequirement itemSkillRequirement = stack.get(DataComponentRegistry.SKILL_REQUIREMENT.get());

        event.getToolTip().clear();
        if (itemRarity == null) itemRarity = RarityTypes.COMMON;
        if (itemType == null) itemType = ItemTypes.ITEM;

        tooltipComponents.add(Component.translatable(stack.getItemName().getString()).withColor(itemRarity.getColor(1.0f)));
        ItemAttributeModifiers modifiers = stack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);

        if (itemDescriptionKey != null) {
            tooltipComponents.add(parseStyledText(Component.translatable(itemDescriptionKey).getString()));
        }

        for (ItemAttributeModifiers.Entry entry : modifiers.modifiers()) {
            Holder<Attribute> attributeHolder = entry.attribute();
            AttributeModifier modifier = entry.modifier();
            UnshatteredAttributes modAttribute = UnshatteredAttributes.fromAttribute(attributeHolder.value());

            if (modAttribute == null) continue;
            tooltipComponents.add(Component.translatable(attributeHolder.value().getDescriptionId()).append(Component.literal(": ")).withStyle(ChatFormatting.GRAY).append(Component.literal("+" + (int) modifier.amount()).withStyle((modAttribute.offensive) ? ChatFormatting.RED : ChatFormatting.GREEN)));
        }

        tooltipComponents.add(Component.empty());
        if (itemType.reforgeable()) tooltipComponents.add(Component.translatable("tooltip.unshattered.reforgable").withColor(0xFF555555));
        if (itemSkillRequirement != null) {
            PlayerSkillsAttachment.Skill requiredSkill = itemSkillRequirement.skill();
            PlayerSkillsAttachment playerSkill = player.getData(AttachmentRegistry.PLAYER_SKILLS.get());
            if (itemSkillRequirement.level() > playerSkill.getLevel(playerSkill.getExp(requiredSkill))) tooltipComponents.add(Component.literal("❣ ").withColor(0xFFAA0000).append(Component.literal("Requires ").withColor(0xFFFF5555)).append(Component.translatable(itemSkillRequirement.skill().getName()).append(" Skill ").append(String.valueOf(itemSkillRequirement.level())).withColor(0xFF55FF55)));
        }

        tooltipComponents.add(Component.literal(Component.translatable("rarity.unshattered." + itemRarity.name().toLowerCase()).getString().toUpperCase() + " " + Component.translatable(itemType.getKey()).getString().toUpperCase()).withColor(itemRarity.getColor(1.0f)).withStyle(ChatFormatting.BOLD));
    }

    public static Component parseStyledText(String input) {
        MutableComponent result = Component.empty();
        Matcher matcher = Pattern.compile("\\[color=(0x[0-9A-Fa-f]+)](.*?)\\[/color]|\\[i](.*?)\\[/i]").matcher(input);
        final int BASE_COLOR = 0xFFAAAAAA;
        int lastEnd = 0;

        while (matcher.find()) {
            if (matcher.start() > lastEnd) {
                String before = input.substring(lastEnd, matcher.start());
                result.append(Component.literal(before).withColor(BASE_COLOR));
            }
            if (matcher.group(1) != null) {
                String colorHex = matcher.group(1);
                String text = matcher.group(2);
                int color = (int) Long.parseLong(colorHex.substring(2), 16);

                result.append(Component.literal(text).withColor(color));
            } else if (matcher.group(3) != null) {
                String text = matcher.group(3);
                result.append(Component.literal(text).withColor(BASE_COLOR).withStyle(ChatFormatting.ITALIC));
            }
            lastEnd = matcher.end();
        }

        if (lastEnd < input.length()) {
            String remaining = input.substring(lastEnd);
            result.append(Component.literal(remaining).withColor(BASE_COLOR));
        }

        return result;
    }
}
