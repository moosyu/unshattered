package io.github.moosyu.commands;

import com.mojang.brigadier.CommandDispatcher;
import io.github.moosyu.attachments.UnshatteredAttachments;
import io.github.moosyu.attachments.PlayerCurrencyAttachment;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class GetCoinsCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("get_coins").executes(context -> {
            ServerPlayer player = context.getSource().getPlayer();
            if (player == null) {
                return 0;
            }
            PlayerCurrencyAttachment coins = player.getData(UnshatteredAttachments.PLAYER_CURRENCY.get());
            context.getSource().sendSuccess(() ->
                            Component.literal(String.valueOf(coins.getCoins())),
                    false);
            return 1;
        }));
    }
}
