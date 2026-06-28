package io.github.moosyu.events;

import io.github.moosyu.screens.ProfileScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;

import static io.github.moosyu.Unshattered.MODID;

// todo: do this properly https://docs.neoforged.net/docs/misc/keymappings/
@EventBusSubscriber(modid = MODID)
public class InputHandler {
    @SubscribeEvent
    public static void onInput(InputEvent.Key event) {
        // make unable to be triggered while in screens (like chatting)
        if (Minecraft.getInstance().screen != null) return;
        Player player = Minecraft.getInstance().player;
        // 82 is r
        if (event.getKey() == 82) Minecraft.getInstance().setScreen(new ProfileScreen());
    }
}
