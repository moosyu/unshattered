package io.github.moosyu.packets.handlers;

import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.packets.UpdateDialogueStatePacket;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class UpdateDialogueStateHandler {
    public static void handleData(final UpdateDialogueStatePacket data, final IPayloadContext context) {
        Player player = context.player();
        player.getData(UnshatteredAttachments.PLAYER_STATE).setDialogueOpen(data.opened());
        player.syncData(UnshatteredAttachments.PLAYER_STATE);
    }
}
