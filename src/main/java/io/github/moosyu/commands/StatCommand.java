package io.github.moosyu.commands;

import com.mojang.brigadier.CommandDispatcher;
import io.github.moosyu.attachments.PlayerSkillsAttachment;
import io.github.moosyu.attachments.PlayerStatsAttachment;
import io.github.moosyu.registers.AttachmentRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class StatCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("health").executes(context -> {
            ServerPlayer player = context.getSource().getPlayer();
            if (player == null) {
                return 0;
            }
            PlayerStatsAttachment stats = player.getData(AttachmentRegistry.PLAYER_STATS.get());
            context.getSource().sendSuccess(() -> net.minecraft.network.chat.Component.literal(String.valueOf(stats.getCurrentStat(PlayerStatsAttachment.Stat.HEALTH))),
                    false);
            return 1;
        }));
    }
}
