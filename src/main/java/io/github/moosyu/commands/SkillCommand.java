package io.github.moosyu.commands;

import com.mojang.brigadier.CommandDispatcher;
import io.github.moosyu.attachments.PlayerSkillsAttachment;
import io.github.moosyu.registers.AttachmentRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
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
                            net.minecraft.network.chat.Component.literal("Foraging exp: " + skills.getForagingExp() + "\n" +
                                    "Combat exp: " + skills.getCombatExp() + "\n" +
                                    "Farming exp: " + skills.getFarmingExp() + "\n" +
                                    "Fishing exp: " + skills.getFishingExp() + "\n" +
                                    "Mining exp: " + skills.getMiningExp()),
                    false);
            return 1;
        }));
    }
}
