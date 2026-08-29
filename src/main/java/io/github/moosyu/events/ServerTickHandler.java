package io.github.moosyu.events;

import io.github.moosyu.data.regen.RegenSavedData;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.ArrayList;
import java.util.Map;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class ServerTickHandler {
    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();
        RegenSavedData regenSavedData = server.overworld().getDataStorage().computeIfAbsent(RegenSavedData.ID);

        Map<GlobalPos, RegenSavedData.RegenState> regen = regenSavedData.regenQueue;
        for (Map.Entry<GlobalPos, RegenSavedData.RegenState> entry : new ArrayList<>(regen.entrySet())) {
            if (regenSavedData.tickBlock(entry.getKey())) {
                regenSavedData.regenerateBlock(entry.getKey(), server.getLevel(entry.getKey().dimension()));
            }
        }
    }
}
