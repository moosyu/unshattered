package io.github.moosyu.data.dialogue;

import com.mojang.serialization.MapCodec;
import net.minecraft.server.level.ServerPlayer;

public interface DialogueTriggeredEvent {
    void trigger(ServerPlayer player);

    MapCodec<? extends DialogueTriggeredEvent> codec();
}
