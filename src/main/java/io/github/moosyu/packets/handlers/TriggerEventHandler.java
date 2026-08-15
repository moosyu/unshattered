package io.github.moosyu.packets.handlers;

import io.github.moosyu.data.dialogue.DialogueEvents;
import io.github.moosyu.data.dialogue.DialogueTriggeredEvent;
import io.github.moosyu.packets.TriggerEventPacket;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class TriggerEventHandler {
    public static void handleData(final TriggerEventPacket data, final IPayloadContext context) {
        if (context.player() instanceof ServerPlayer serverPlayer) {
            DialogueTriggeredEvent dialogueTriggeredEvent = DialogueEvents.getDialogueEvent(data.dialogueEventIdentifier());
            if (dialogueTriggeredEvent == null) return;

            DialogueEvents.getDialogueEvent(data.dialogueEventIdentifier()).trigger(serverPlayer);
        }
    }
}
