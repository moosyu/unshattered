package io.github.moosyu.packets;

import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jspecify.annotations.NonNull;

public record UpdateStorageScrollPacket(boolean scrolledDown) implements CustomPacketPayload {
    public static final Type<UpdateStorageScrollPacket> TYPE = new Type<>(UnshatteredUtils.getUnshatteredIdentifier("storage_scroll_fired"));
    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateStorageScrollPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, UpdateStorageScrollPacket::scrolledDown,
            UpdateStorageScrollPacket::new
    );

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
