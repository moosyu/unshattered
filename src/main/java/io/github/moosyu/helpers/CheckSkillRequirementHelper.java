package io.github.moosyu.helpers;

import io.github.moosyu.attachments.PlayerSkillsAttachment;
import io.github.moosyu.data.components.SkillRequirement;
import io.github.moosyu.registers.AttachmentRegistry;
import io.github.moosyu.registers.DataComponentRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.Tags;

public final class CheckSkillRequirementHelper {
    public static boolean canUseItem(Player player, ItemStack itemUsed) {
        SkillRequirement itemSkillRequirement = itemUsed.get(DataComponentRegistry.SKILL_REQUIREMENT.get());
        // armors already have their own logic in LivingEquipmentChangeHandler
        if (itemSkillRequirement == null || itemUsed.is(Tags.Items.ARMORS)) return true;
        PlayerSkillsAttachment playerSkills = player.getData(AttachmentRegistry.PLAYER_SKILLS.get());
        if (playerSkills.getLevel(playerSkills.getExp(itemSkillRequirement.skill())) < itemSkillRequirement.level()) {
            player.sendSystemMessage(Component.literal(itemSkillRequirement.skill().getName() + " level " + itemSkillRequirement.level() + " is required to use this item!").withColor(0xFFFF5555));
            return false;
        }
        return true;
    }
}
