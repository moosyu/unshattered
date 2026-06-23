package io.github.moosyu.events;

import io.github.moosyu.skills.fishing.FishingManager;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class ServerReloadListenersHandler {
    @SubscribeEvent
    public static void onServerReloadListeners(AddServerReloadListenersEvent event) {
        event.addListener(Identifier.fromNamespaceAndPath(MODID, "fishing_manager"), new FishingManager());
    }
}
