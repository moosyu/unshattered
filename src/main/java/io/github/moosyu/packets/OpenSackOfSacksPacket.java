package io.github.moosyu.packets;

import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jspecify.annotations.NonNull;

public record OpenSackOfSacksPacket() implements CustomPacketPayload {
    public static final Type<OpenSackOfSacksPacket> TYPE = new Type<>(UnshatteredUtils.getUnshatteredIdentifier("open_sack_of_sacks_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, OpenSackOfSacksPacket> STREAM_CODEC = StreamCodec.unit(new OpenSackOfSacksPacket());

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
