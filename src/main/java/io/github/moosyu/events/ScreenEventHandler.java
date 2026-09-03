package io.github.moosyu.events;

import io.github.moosyu.gui.menus.UnshatteredMenus;
import io.github.moosyu.gui.screens.DialogueScreen;
import io.github.moosyu.packets.OpenTalismanBagPacket;
import io.github.moosyu.packets.UpdateDialogueStatePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
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
//            event.setCanceled(true);
//            ClientPacketDistributor.sendToServer(new OpenTalismanBagPacket());
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

    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event) {
        if ((event.getScreen() instanceof InventoryScreen screen)) {
            screen.children().stream()
                    .filter(widget -> widget instanceof ImageButton)
                    .findFirst()
                    .ifPresent(screen::removeWidget);
        }
    }
}
