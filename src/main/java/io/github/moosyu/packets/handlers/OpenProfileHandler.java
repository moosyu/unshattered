package io.github.moosyu.packets.handlers;

import io.github.moosyu.gui.screens.ProfileScreen;
import io.github.moosyu.packets.OpenProfilePayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class OpenProfileHandler {
    public static void handleData(final OpenProfilePayload data, final IPayloadContext context) {
        if (context.player() instanceof ServerPlayer serverPlayer) {
            ProfileScreen.openProfile(serverPlayer);
        }
    }
}
