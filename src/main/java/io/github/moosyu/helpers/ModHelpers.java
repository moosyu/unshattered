package io.github.moosyu.helpers;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

// just some small things that i get tired of writing during development, not actually for use in release
public class ModHelpers {
    public static void broadcastMessage(String message) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null) {
            minecraft.player.sendSystemMessage(Component.literal(message));
        }
    }
}
