package io.github.moosyu.commands;

import com.mojang.brigadier.CommandDispatcher;
import io.github.moosyu.data.attachments.PlayerDialogueFlagsAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class ResetFlagsCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("reset_flags").executes(context -> {
            ServerPlayer player = context.getSource().getPlayer();
            if (player == null) {
                return 0;
            }
            PlayerDialogueFlagsAttachment dialogueFlagsAttachment = player.getData(UnshatteredAttachments.PLAYER_DIALOGUE_FLAGS.get());
            dialogueFlagsAttachment.resetFlags(player);
            context.getSource().sendSuccess(() ->
                            Component.literal("reset dialogue flags!"),
                    true);
            return 1;
        }));
    }
}
