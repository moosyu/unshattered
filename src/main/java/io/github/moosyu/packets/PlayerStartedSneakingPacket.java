package io.github.moosyu.packets;

import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jspecify.annotations.NonNull;

public record PlayerStartedSneakingPacket()implements CustomPacketPayload {
    public static final Type<PlayerStartedSneakingPacket> TYPE = new Type<>(UnshatteredUtils.getUnshatteredIdentifier("player_started_sneaking"));
    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerStartedSneakingPacket> STREAM_CODEC = StreamCodec.unit(new PlayerStartedSneakingPacket());

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}