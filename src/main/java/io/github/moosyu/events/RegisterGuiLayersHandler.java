package io.github.moosyu.events;

import io.github.moosyu.layers.HealthBarLayer;
import io.github.moosyu.layers.ManaBarLayer;
import io.github.moosyu.layers.SelectedItemLayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class RegisterGuiLayersHandler {
    @SubscribeEvent
    public static void onRegisterGuiLayer(RegisterGuiLayersEvent event) {
        event.replaceLayer(VanillaGuiLayers.PLAYER_HEALTH, new HealthBarLayer());
        event.replaceLayer(VanillaGuiLayers.FOOD_LEVEL, new ManaBarLayer());
        event.replaceLayer(VanillaGuiLayers.SELECTED_ITEM_NAME, new SelectedItemLayer());
        // hiding some vanilla bits
        event.replaceLayer(VanillaGuiLayers.CONTEXTUAL_INFO_BAR, (_, _) -> {});
        event.replaceLayer(VanillaGuiLayers.EXPERIENCE_LEVEL, (_, _) -> {});
        event.replaceLayer(VanillaGuiLayers.CONTEXTUAL_INFO_BAR_BACKGROUND, (_, _) -> {});
        event.replaceLayer(VanillaGuiLayers.ARMOR_LEVEL, (_, _) -> {});
    }
}
