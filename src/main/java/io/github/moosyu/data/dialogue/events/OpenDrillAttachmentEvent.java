package io.github.moosyu.data.dialogue.events;

import com.mojang.serialization.MapCodec;
import io.github.moosyu.data.dialogue.DialogueTriggeredEvent;
import io.github.moosyu.gui.menus.DrillAttachmentMenu;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ContainerLevelAccess;

public record OpenDrillAttachmentEvent() implements DialogueTriggeredEvent {
    @Override
    public void trigger(ServerPlayer player) {
        player.openMenu(new SimpleMenuProvider(
                (containerId, inventory, _) -> new DrillAttachmentMenu(containerId, inventory),
                Component.translatable("container.unshattered.drill_attachments")
        ));
    }

    @Override
    public MapCodec<? extends DialogueTriggeredEvent> codec() {
        return CODEC;
    }

    public static MapCodec<OpenDrillAttachmentEvent> CODEC = MapCodec.unit(OpenDrillAttachmentEvent::new);
    public static StreamCodec<ByteBuf, OpenDrillAttachmentEvent> STREAM_CODEC = StreamCodec.unit(new OpenDrillAttachmentEvent());
}
