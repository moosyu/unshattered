package io.github.moosyu.events;

import io.github.moosyu.packets.PlayerStartedSneakingPacket;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class ClientInputUpdateHandler {
    @SubscribeEvent
    public static void onInputUpdate(MovementInputUpdateEvent event) {
        if (event.getInput().keyPresses.shift()) {
            ClientPacketDistributor.sendToServer(new PlayerStartedSneakingPacket());
        }
    }
}
