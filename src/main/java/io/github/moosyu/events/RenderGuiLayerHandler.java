package io.github.moosyu.events;

import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

import static io.github.moosyu.Unshattered.MODID;

public class RenderGuiLayerHandler {
    @EventBusSubscriber(modid = MODID)
    public static class EventHandler {
        @SubscribeEvent
        public static void onRegisterGuiLayer(RenderGuiLayerEvent.Pre event) {
            // move the item name up so it doesnt touch the bar
            if (event.getName() == VanillaGuiLayers.SELECTED_ITEM_NAME) {
                GuiGraphics guiGraphics = event.getGuiGraphics();
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate(0, -10, 0);
            }

        }
    }
}
