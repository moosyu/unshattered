package io.github.moosyu.data.dialogue.events;

import com.mojang.serialization.MapCodec;
import io.github.moosyu.data.dialogue.DialogueTriggeredEvent;
import io.github.moosyu.gui.menus.ForgeMenu;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;

public record OpenForgeEvent() implements DialogueTriggeredEvent {
    @Override
    public void trigger(ServerPlayer player) {
        player.openMenu(new SimpleMenuProvider((containerId, inventory, _) -> new ForgeMenu(containerId, inventory),
                        Component.translatable("screen.unshattered.forge")
                )
        );
    }

    @Override
    public MapCodec<? extends DialogueTriggeredEvent> codec() {
        return CODEC;
    }

    public static MapCodec<OpenForgeEvent> CODEC = MapCodec.unit(OpenForgeEvent::new);
    public static StreamCodec<ByteBuf, OpenForgeEvent> STREAM_CODEC = StreamCodec.unit(new OpenForgeEvent());
}
