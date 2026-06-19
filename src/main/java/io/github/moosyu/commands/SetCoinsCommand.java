package io.github.moosyu.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import io.github.moosyu.attachments.UnshatteredAttachments;
import io.github.moosyu.attachments.PlayerCurrencyAttachment;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class SetCoinsCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("set_coins").then(Commands.argument("amount", IntegerArgumentType.integer())
            .executes(context -> {
                ServerPlayer player = context.getSource().getPlayerOrException();
                PlayerCurrencyAttachment coins = player.getData(UnshatteredAttachments.PLAYER_CURRENCY.get());
                int amount = IntegerArgumentType.getInteger(context, "amount");
                coins.setCoins(amount);
                player.syncData(UnshatteredAttachments.PLAYER_CURRENCY.get());
                context.getSource().sendSuccess(
                        () -> Component.literal("coins set to " + amount),
                        false
                );
                return 1;
            }))
        );
    }
}
