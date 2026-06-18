package io.github.moosyu.events;

import io.github.moosyu.attachments.PlayerSkillsAttachment;
import io.github.moosyu.attributes.UnshatteredAttributes;
import io.github.moosyu.data.components.ItemCharges;
import io.github.moosyu.data.components.SkillRequirement;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.helpers.TextHelpers;
import io.github.moosyu.items.ItemTypes;
import io.github.moosyu.rarities.RarityTypes;
import io.github.moosyu.attachments.AttachmentRegistry;
import io.github.moosyu.data.components.DataComponentRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.List;

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
        ItemAbility itemAbility = stack.get(DataComponentRegistry.ITEM_ABILITY);
        ItemCharges itemCharges = stack.get(DataComponentRegistry.ITEM_CHARGES);

        event.getToolTip().clear();
        if (itemRarity == null) itemRarity = RarityTypes.COMMON;
        if (itemType == null) itemType = ItemTypes.ITEM;

        tooltipComponents.add(Component.translatable(stack.getItemName().getString()).withColor(itemRarity.getColor(1.0f)));
        ItemAttributeModifiers modifiers = stack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);

        for (ItemAttributeModifiers.Entry entry : modifiers.modifiers()) {
            Holder<Attribute> attributeHolder = entry.attribute();
            AttributeModifier modifier = entry.modifier();
            UnshatteredAttributes modAttribute = UnshatteredAttributes.fromAttribute(attributeHolder.value());

            if (modAttribute == null) continue;
            tooltipComponents.add(Component.translatable(attributeHolder.value().getDescriptionId()).append(Component.literal(": ")).withStyle(ChatFormatting.GRAY).append(Component.literal("+" + (int) modifier.amount() + (modAttribute.percentage ? "%" : "")).withStyle((modAttribute.offensive) ? ChatFormatting.RED : ChatFormatting.GREEN)));
        }

        if (itemDescriptionKey != null) {
            tooltipComponents.add(TextHelpers.parseStyledText(Component.translatable(itemDescriptionKey).getString(), 0xFFAAAAAA));
        }

        if (itemAbility != null) {
            tooltipComponents.add(Component.literal(""));
            if (itemAbility.passive()) {
                tooltipComponents.add(Component.literal("Ability: ").append(Component.translatable("item.ability.unshattered." + itemAbility.abilityId())).withColor(0xFFFFAA00));
                tooltipComponents.add(TextHelpers.parseStyledText(Component.translatable("item.ability.description.unshattered." + itemAbility.abilityId()).getString(), 0xFFAAAAAA));
            } else {
                tooltipComponents.add(Component.literal("Ability: ").append(Component.translatable("item.ability.unshattered." + itemAbility.abilityId())).withColor(0xFFFFAA00).append(Component.literal(" RIGHT CLICK").withColor(0xFFFFFF55).withStyle(ChatFormatting.BOLD)));
                tooltipComponents.add(TextHelpers.parseStyledText(Component.translatable("item.ability.description.unshattered." + itemAbility.abilityId()).getString(), 0xFFAAAAAA));

                if (itemAbility.manaCost() > 0) tooltipComponents.add(Component.literal("Mana Cost: ").withColor(0xFF555555).append(Component.literal(String.valueOf(itemAbility.manaCost())).withColor(0xFF00AAAA)));
                if (itemAbility.cooldown() > 0) tooltipComponents.add(Component.literal("Cooldown: ").withColor(0xFF555555).append(Component.literal(String.format("%.1f", (float) itemAbility.cooldown() / 20 /* convert ticks to seconds */)).append("s").withColor(0xFF55FF55)));
                if (itemCharges != null) tooltipComponents.add(Component.literal("Charges: ").withColor(0xFF555555).append(Component.literal(String.valueOf(itemCharges.currentCharges())).withColor(0xFFFFFF55)).append(Component.literal("/").withColor(0xFF555555)).append(Component.literal((itemCharges.rechargeTime() / 20) + "s").withColor(0xFF55FF55)));
            }
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
}
