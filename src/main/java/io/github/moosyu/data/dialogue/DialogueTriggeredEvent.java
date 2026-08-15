package io.github.moosyu.data.dialogue;

import net.minecraft.server.level.ServerPlayer;

public interface DialogueTriggeredEvent {
    void trigger(ServerPlayer player);
}
