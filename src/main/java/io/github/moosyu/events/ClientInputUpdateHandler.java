package io.github.moosyu.events;

import io.github.moosyu.packets.PlayerStartedSneakingPacket;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.MovementInputUpdateEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class ClientInputUpdateHandler {
    private static boolean sneaking = false;

    @SubscribeEvent
    public static void onInputUpdate(MovementInputUpdateEvent event) {
        boolean isSneaking = event.getInput().keyPresses.shift();
        if (isSneaking && !sneaking) {
            ClientPacketDistributor.sendToServer(new PlayerStartedSneakingPacket());
        }
        sneaking = isSneaking;
    }
}
