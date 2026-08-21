package io.github.moosyu.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

import static io.github.moosyu.Unshattered.MODID;

public record UpdateDialogueStatePacket(boolean opened) implements CustomPacketPayload {
    public static final Type<UpdateDialogueStatePacket> TYPE = new Type<>(Identifier.fromNamespaceAndPath(MODID, "close_dialogue_packet"));
    public static final StreamCodec<ByteBuf, UpdateDialogueStatePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, UpdateDialogueStatePacket::opened,
            UpdateDialogueStatePacket::new
    );

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
