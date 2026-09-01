package io.github.moosyu.events;

import io.github.moosyu.data.regen.RegenSavedData;
import io.github.moosyu.packets.BlockBreakSyncPacket;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.ChunkWatchEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Map;

import static io.github.moosyu.Unshattered.MODID;

// note: might need to add a chunk unwatch event to clear
@EventBusSubscriber(modid = MODID)
public class ChunkWatchHandler {
    public static void onChunkWatch(ChunkWatchEvent.Watch event) {
        ServerLevel level = event.getLevel();
        RegenSavedData data = level.getDataStorage().computeIfAbsent(RegenSavedData.ID);
        ChunkPos watchedChunk = event.getPos();

        for (Map.Entry<GlobalPos, RegenSavedData.RegenState> entry : data.regenQueue.entrySet()) {
            GlobalPos globalPos = entry.getKey();
            if (globalPos.dimension().equals(level.dimension()) && new ChunkPos(globalPos.pos().getX(), globalPos.pos().getZ()).equals(watchedChunk)) {
                PacketDistributor.sendToPlayer(event.getPlayer(), new BlockBreakSyncPacket(globalPos.pos(), entry.getValue().regenPathIdentifier(), entry.getValue().regenPathIndex()));
            }
        }

    }
}
