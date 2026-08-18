package io.github.moosyu.packets.handlers;

import io.github.moosyu.packets.TriggerEventPacket;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class TriggerEventHandler {
    public static void handleData(final TriggerEventPacket data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer) {
                data.dialogueTriggeredEvent().trigger(serverPlayer);
            }
        });
    }
}
