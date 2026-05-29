package io.github.moosyu.events;

import io.github.moosyu.screens.ProfileScreen;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;

import static io.github.moosyu.Unshattered.MODID;

public class InputHandler {
    @EventBusSubscriber(modid = MODID)
    public static class EventHandler {
        @SubscribeEvent
        public static void onInput(InputEvent.Key event) {
            // make unable to be triggered while in screens (like chatting)
            if (Minecraft.getInstance().screen != null) return;
            // 75 is k, this is just temporary as the intended
            if (event.getKey() == 75) Minecraft.getInstance().setScreen(new ProfileScreen());
        }
    }
}
