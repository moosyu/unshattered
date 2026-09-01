package io.github.moosyu.events;

import io.github.moosyu.data.regen.RegenClientCache;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class ClientPlayerNetworkHandler {
    public static void onClientPlayerLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        // not even sure if this matters but better safe than sorry
        RegenClientCache.clear();
    }
}
