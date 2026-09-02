package io.github.moosyu.events;

import io.github.moosyu.gui.screens.DialogueScreen;
import io.github.moosyu.packets.UpdateDialogueStatePacket;
import io.github.moosyu.packets.OpenProfilePayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class ScreenEventHandler {
    @SubscribeEvent
    public static void onScreenEventOpen(ScreenEvent.Opening event) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        // override inventory
        if (event.getNewScreen() instanceof InventoryScreen && !player.isCreative()) {
            // event.setCanceled(true);
            // ClientPacketDistributor.sendToServer(new OpenProfilePayload());
        } else if (event.getScreen() instanceof DialogueScreen) {
            ClientPacketDistributor.sendToServer(new UpdateDialogueStatePacket(true));
        }
    }

    @SubscribeEvent
    public static void onScreenEventClose(ScreenEvent.Closing event) {
        if (Minecraft.getInstance().player == null) return;

        if (event.getScreen() instanceof DialogueScreen) {
            ClientPacketDistributor.sendToServer(new UpdateDialogueStatePacket(false));
        }
    }

}
