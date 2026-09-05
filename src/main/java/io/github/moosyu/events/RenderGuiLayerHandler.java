package io.github.moosyu.events;

import io.github.moosyu.gui.screens.DialogueScreen;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class RenderGuiLayerHandler {
    @SubscribeEvent
    public static void onRenderGuiPre(RenderGuiLayerEvent.Pre event) {
        if (Minecraft.getInstance().screen instanceof DialogueScreen && event.getName() == VanillaGuiLayers.HOTBAR) {
            event.setCanceled(true);
        }
    }
}
