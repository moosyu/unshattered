package io.github.moosyu.packets;

import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jspecify.annotations.NonNull;

public record OpenReforgeAnvilPacket() implements CustomPacketPayload {
    public static final Type<OpenReforgeAnvilPacket> TYPE = new Type<>(UnshatteredUtils.getUnshatteredIdentifier("open_reforge_anvil"));
    public static final StreamCodec<RegistryFriendlyByteBuf, OpenReforgeAnvilPacket> STREAM_CODEC = StreamCodec.unit(new OpenReforgeAnvilPacket());

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
