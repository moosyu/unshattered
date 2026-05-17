package io.github.moosyu.events;

import io.github.moosyu.layers.DefenseBarLayer;
import io.github.moosyu.layers.ExperienceBarLayer;
import io.github.moosyu.layers.HealthBarLayer;
import io.github.moosyu.layers.ManaBarLayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

import static io.github.moosyu.NNO.MODID;

public class RegisterGuiLayersHandler {
    @EventBusSubscriber(modid = MODID)
    public static class EventHandler {
        @SubscribeEvent
        public static void onRegisterGuiLayer(RegisterGuiLayersEvent event) {
            System.out.println(event);
            event.replaceLayer(VanillaGuiLayers.PLAYER_HEALTH, new HealthBarLayer());
            event.replaceLayer(VanillaGuiLayers.FOOD_LEVEL, new ManaBarLayer());
            event.replaceLayer(VanillaGuiLayers.EXPERIENCE_BAR, new ExperienceBarLayer());
            event.replaceLayer(VanillaGuiLayers.ARMOR_LEVEL, new DefenseBarLayer());
        }
    }
}
