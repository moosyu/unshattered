package io.github.moosyu.events;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.gamerules.GameRules;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;

import static io.github.moosyu.Unshattered.MODID;

// so stuff like vines spreading or wheat realising its in the wrong place doesnt happen
@EventBusSubscriber(modid = MODID)
public class ServerAboutToStartHandler {
    @SubscribeEvent
    public static void onServerAboutToStart(ServerAboutToStartEvent event) {
        MinecraftServer server = event.getServer();
        GameRules gameRule = server.getGameRules();

        gameRule.set(GameRules.RANDOM_TICK_SPEED, 0, server);
    }
}
