package io.github.moosyu.commands;

import com.mojang.brigadier.CommandDispatcher;
import io.github.moosyu.attachments.PlayerSkillsAttachment;
import io.github.moosyu.registers.AttachmentRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class SkillCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("skills").executes(context -> {
            ServerPlayer player = context.getSource().getPlayer();
            if (player == null) {
                return 0;
            }
            PlayerSkillsAttachment skills = player.getData(AttachmentRegistry.PLAYER_SKILLS.get());
            context.getSource().sendSuccess(() ->
                            Component.literal("Foraging exp: " + skills.getExp(PlayerSkillsAttachment.Skill.FORAGING) + "\n" +
                                    "Combat exp: " + skills.getExp(PlayerSkillsAttachment.Skill.COMBAT) + "\n" +
                                    "Farming exp: " + skills.getExp(PlayerSkillsAttachment.Skill.FARMING) + "\n" +
                                    "Fishing exp: " + skills.getExp(PlayerSkillsAttachment.Skill.FISHING) + "\n" +
                                    "Mining exp: " + skills.getExp(PlayerSkillsAttachment.Skill.MINING)
                            ),
                    false);
            return 1;
        }));
    }
}
