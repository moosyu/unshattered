package io.github.moosyu.packets.handlers;

import io.github.moosyu.data.attachments.PlayerDialogueFlagsAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.packets.ResetFlagQueuePacket;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ResetFlagQueueHandler {
    public static void handleData(final ResetFlagQueuePacket data, final IPayloadContext context) {
        if (context.player() instanceof ServerPlayer serverPlayer) {
            PlayerDialogueFlagsAttachment playerDialogueFlagsAttachment = serverPlayer.getData(UnshatteredAttachments.PLAYER_DIALOGUE_FLAGS);
            if (data.addToPlayerFlags()) {
                playerDialogueFlagsAttachment.addQueuedFlags();
                serverPlayer.syncData(UnshatteredAttachments.PLAYER_DIALOGUE_FLAGS);
            } else {
                playerDialogueFlagsAttachment.clearFlagQueue();
            }
        }
    }
}
