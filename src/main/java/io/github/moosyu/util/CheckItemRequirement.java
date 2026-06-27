package io.github.moosyu.util;

import io.github.moosyu.attachments.PlayerSkillsAttachment;
import io.github.moosyu.attachments.PlayerStateAttachment;
import io.github.moosyu.data.components.ItemCharges;
import io.github.moosyu.data.components.SkillRequirement;
import io.github.moosyu.attachments.UnshatteredAttachments;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.Tags;

public final class CheckItemRequirement {
    public static final int ERROR_COLOR = 0xFFFF5555;

    public static boolean passesSkillCheck(Player player, ItemStack itemUsed) {
        SkillRequirement itemSkillRequirement = itemUsed.get(UnshatteredDataComponents.SKILL_REQUIREMENT.get());
        // armours already have their own logic in LivingEquipmentChangeHandler
        if (itemSkillRequirement == null || itemUsed.is(Tags.Items.ARMORS)) return true;
        PlayerSkillsAttachment playerSkills = player.getData(UnshatteredAttachments.PLAYER_SKILLS.get());
        if (playerSkills.getLevel(playerSkills.getExp(itemSkillRequirement.skill())) < itemSkillRequirement.level()) {
            player.sendSystemMessage(Component.literal(Component.translatable(itemSkillRequirement.skill().getTranslationKey()).getString() + " level " + itemSkillRequirement.level() + " is required to use this item!").withColor(ERROR_COLOR));
            return false;
        }
        return true;
    }

    public static boolean passesManaCheck(Player player, int manaCost) {
        if (player.getData(UnshatteredAttachments.PLAYER_STATE.get()).getCurrentStat(PlayerStateAttachment.Stat.MANA) < manaCost) {
            player.sendSystemMessage(Component.literal("You don't have enough mana to use this!").withColor(ERROR_COLOR));
            return false;
        }
        return true;
    }

    public static boolean passesChargesCheck(Player player, ItemCharges itemCharges, Identifier rechargeIdentifier) {
        if (itemCharges.currentCharges() <= 0) {
            player.sendSystemMessage(
                    Component.literal("You don't have any charges left! Wait " + (((player.getData(UnshatteredAttachments.PLAYER_ABILITIES.get()).expiryTimeTicks(rechargeIdentifier) - player.level().getGameTime()) / 20) + 1) + "s.")
                    .withColor(ERROR_COLOR));
            return false;
        }
        return true;
    }
}
