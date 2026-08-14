package io.github.moosyu.events;

import io.github.moosyu.gui.screens.DialogueScreen;
import io.github.moosyu.packets.ResetFlagQueuePacket;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class ScreenEventsHandler {
    @SubscribeEvent
    public static void onScreenClosing(ScreenEvent.Closing event) {
        if (event.getScreen() instanceof DialogueScreen) {
            ClientPacketDistributor.sendToServer(new ResetFlagQueuePacket(false));
        }
    }
}