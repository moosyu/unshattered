package io.github.moosyu.packets;

import io.github.moosyu.Unshattered;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

import java.util.List;

public record QueueNewFlagsPacket(List<Identifier> flags) implements CustomPacketPayload {
    public static final Type<QueueNewFlagsPacket> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Unshattered.MODID, "queue_new_flags"));
    public static final StreamCodec<RegistryFriendlyByteBuf, QueueNewFlagsPacket> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC.apply(ByteBufCodecs.list()), QueueNewFlagsPacket::flags,
            QueueNewFlagsPacket::new
    );

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
