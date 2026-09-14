package io.github.moosyu.events;

import io.github.moosyu.gui.screens.DialogueScreen;
import io.github.moosyu.gui.screens.UnshatteredInventoryScreen;
import io.github.moosyu.packets.UpdateDialogueStatePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CraftingScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
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
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null) return;

        // override inventory
        if (event.getNewScreen() instanceof InventoryScreen && !player.isCreative()) {
            event.setCanceled(true);
            Minecraft.getInstance().setScreen(
                    new UnshatteredInventoryScreen(
                            player.inventoryMenu,
                            player.getInventory(),
                            Component.translatable("container.inventory")
                    )
            );
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
    public static void onScreenKeyPressed(ScreenEvent.KeyPressed.Pre event) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (Minecraft.getInstance().player == null) return;

        if (event.getScreen() instanceof CraftingScreen craftingScreen) {
            if (minecraft.options.keyInventory.matches(event.getKeyEvent())) {
                event.setCanceled(true);
                player.connection.send(new ServerboundContainerClosePacket(craftingScreen.getMenu().containerId));
                minecraft.setScreen(new UnshatteredInventoryScreen(player.inventoryMenu, player.getInventory(), Component.translatable("container.inventory")));
            }
        }
    }
}
