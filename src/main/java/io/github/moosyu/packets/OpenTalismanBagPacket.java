package io.github.moosyu.packets;

import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jspecify.annotations.NonNull;

public record OpenTalismanBagPacket() implements CustomPacketPayload {
    public static final Type<OpenTalismanBagPacket> TYPE = new Type<>(UnshatteredUtils.getUnshatteredIdentifier("open_talisman_bag_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, OpenTalismanBagPacket> STREAM_CODEC = StreamCodec.unit(new OpenTalismanBagPacket());

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
