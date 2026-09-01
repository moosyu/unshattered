package io.github.moosyu.packets.handlers;

import io.github.moosyu.data.regen.RegenClientCache;
import io.github.moosyu.packets.BlockBreakSyncPacket;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class BlockBreakSyncHandler {
    public static void handleData(final BlockBreakSyncPacket data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (data.regenPathIndex() == 0) {
                RegenClientCache.remove(data.pos());
            } else {
                RegenClientCache.put(data.pos(), data.regenPathId(), data.regenPathIndex());
            }
        });
    }
}
