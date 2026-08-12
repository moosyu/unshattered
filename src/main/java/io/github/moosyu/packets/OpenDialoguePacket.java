package io.github.moosyu.packets;

import io.github.moosyu.data.dialogue.DialogueNode;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

import static io.github.moosyu.Unshattered.MODID;

public record OpenDialoguePacket(Component talkableName, DialogueNode selectedDialogueNode) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<OpenDialoguePacket> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(MODID, "open_dialogue_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, OpenDialoguePacket> STREAM_CODEC = StreamCodec.composite(
            ComponentSerialization.STREAM_CODEC, OpenDialoguePacket::talkableName,
            DialogueNode.STREAM_CODEC, OpenDialoguePacket::selectedDialogueNode,
            OpenDialoguePacket::new
    );

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
