package io.github.moosyu.packets.handlers;

import io.github.moosyu.gui.screens.DialogueScreen;
import io.github.moosyu.packets.OpenDialoguePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class OpenDialogueHandler {
    public static void handleData(final OpenDialoguePacket data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            minecraft.setScreen(new DialogueScreen(data.talkableName(), data.selectedDialogueNode(), context.player()));
        });
    }
}
