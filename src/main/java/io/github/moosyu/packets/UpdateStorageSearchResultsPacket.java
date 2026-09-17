package io.github.moosyu.packets;

import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jspecify.annotations.NonNull;

public record UpdateStorageSearchResultsPacket(String input) implements CustomPacketPayload {
    public static final Type<UpdateStorageSearchResultsPacket> TYPE = new Type<>(UnshatteredUtils.getUnshatteredIdentifier("update_storage_search"));
    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateStorageSearchResultsPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, UpdateStorageSearchResultsPacket::input,
            UpdateStorageSearchResultsPacket::new
    );

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
