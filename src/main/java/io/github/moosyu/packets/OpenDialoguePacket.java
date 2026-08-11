package io.github.moosyu.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

import static io.github.moosyu.Unshattered.MODID;

public record OpenDialoguePacket() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<OpenDialoguePacket> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(MODID, "open_dialogue_packet"));
    public static final StreamCodec<ByteBuf, OpenDialoguePacket> STREAM_CODEC = StreamCodec.unit(new OpenDialoguePacket());

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
