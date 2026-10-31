package io.github.moosyu.events;

import io.github.moosyu.gui.components.tabs.TabBar;
import io.github.moosyu.gui.components.tabs.UnshatteredTabs;
import io.github.moosyu.gui.screens.UnshatteredInventoryScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
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

import java.util.Map;
import java.util.WeakHashMap;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class ScreenEventHandler {
    private static final Map<Screen, TabBar> BARS = new WeakHashMap<>();

    @SubscribeEvent
    public static void onScreenEventOpen(ScreenEvent.Opening event) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null) {
            return;
        }

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
        }
    }

    @SubscribeEvent
    static void onInit(ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof CraftingScreen craftingScreen) {
            TabBar bar = new TabBar(UnshatteredTabs.ALL, UnshatteredTabs.CRAFTING, TabBar.GENERIC_OFFSET_TOP, TabBar.GENERIC_OFFSET_BOTTOM);

            int left = (craftingScreen.width - 176) / 2;
            int top  = (craftingScreen.height - 166) / 2;
            bar.init(left, top, event::addListener);
            BARS.put(craftingScreen, bar);
        }
    }

    @SubscribeEvent
    static void onRender(ScreenEvent.Render.Post event) {
        TabBar bar = BARS.get(event.getScreen());
        if (bar != null) {
            bar.displayIcons(event.getGuiGraphics());
        }
    }
}
