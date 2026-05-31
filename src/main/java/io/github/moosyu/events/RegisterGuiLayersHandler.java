package io.github.moosyu.events;

import io.github.moosyu.layers.HealthBarLayer;
import io.github.moosyu.layers.ManaBarLayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

import static io.github.moosyu.Unshattered.MODID;

public class RegisterGuiLayersHandler {
    @EventBusSubscriber(modid = MODID)
    public static class EventHandler {
        @SubscribeEvent
        public static void onRegisterGuiLayer(RegisterGuiLayersEvent event) {
            // https://antongerdelan.net/colour/
            event.replaceLayer(VanillaGuiLayers.PLAYER_HEALTH, new HealthBarLayer());
            event.replaceLayer(VanillaGuiLayers.FOOD_LEVEL, new ManaBarLayer());
            event.replaceLayer(VanillaGuiLayers.SELECTED_ITEM_NAME, new ManaBarLayer());
            // hiding some vanilla bits
            event.replaceLayer(VanillaGuiLayers.CONTEXTUAL_INFO_BAR, (context, tickCounter) -> {});
            event.replaceLayer(VanillaGuiLayers.EXPERIENCE_LEVEL, (context, tickCounter) -> {});
            event.replaceLayer(VanillaGuiLayers.CONTEXTUAL_INFO_BAR_BACKGROUND, (context, tickCounter) -> {});
            event.replaceLayer(VanillaGuiLayers.ARMOR_LEVEL, (context, tickCounter) -> {});
        }
    }
}
