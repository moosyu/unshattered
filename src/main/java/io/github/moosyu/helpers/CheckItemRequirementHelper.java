package io.github.moosyu.helpers;

import io.github.moosyu.attachments.PlayerSkillsAttachment;
import io.github.moosyu.attachments.PlayerStateAttachment;
import io.github.moosyu.data.components.SkillRequirement;
import io.github.moosyu.attachments.AttachmentRegistry;
import io.github.moosyu.data.components.DataComponentRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.Tags;

public final class CheckItemRequirementHelper {
    public static final int ERROR_COLOR = 0xFFFF5555;

    public static boolean passesSkillCheck(Player player, ItemStack itemUsed) {
        SkillRequirement itemSkillRequirement = itemUsed.get(DataComponentRegistry.SKILL_REQUIREMENT.get());
        // armours already have their own logic in LivingEquipmentChangeHandler
        if (itemSkillRequirement == null || itemUsed.is(Tags.Items.ARMORS)) return true;
        PlayerSkillsAttachment playerSkills = player.getData(AttachmentRegistry.PLAYER_SKILLS.get());
        if (playerSkills.getLevel(playerSkills.getExp(itemSkillRequirement.skill())) < itemSkillRequirement.level()) {
            player.sendSystemMessage(Component.literal(itemSkillRequirement.skill().getName() + " level " + itemSkillRequirement.level() + " is required to use this item!").withColor(ERROR_COLOR));
            return false;
        }
        return true;
    }

    public static boolean passesManaCheck(Player player, int manaCost) {
        if (player.getData(AttachmentRegistry.PLAYER_STATE.get()).getCurrentStat(PlayerStateAttachment.Stat.MANA) < manaCost) {
            player.sendSystemMessage(Component.literal("You don't have enough mana to use this!").withColor(ERROR_COLOR));
            return false;
        }
        return true;
    }
}
