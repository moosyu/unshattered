package io.github.moosyu.packets;

import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jspecify.annotations.NonNull;

public record UpdateStoragePagePacket(boolean increment) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<UpdateStoragePagePacket> TYPE = new CustomPacketPayload.Type<>(UnshatteredUtils.getUnshatteredIdentifier("update_storage_page"));
    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateStoragePagePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, UpdateStoragePagePacket::increment,
            UpdateStoragePagePacket::new
    );

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
