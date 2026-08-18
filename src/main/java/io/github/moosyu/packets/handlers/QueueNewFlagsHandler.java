package io.github.moosyu.packets.handlers;

import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.packets.QueueNewFlagsPacket;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class QueueNewFlagsHandler {
    public static void handleData(final QueueNewFlagsPacket data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer) {
                serverPlayer.getData(UnshatteredAttachments.PLAYER_DIALOGUE_FLAGS.get()).addFlagsToQueue(data.flags());
            }
        });
    }
}