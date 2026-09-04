package io.github.moosyu.packets;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record OpenStoragePacket() implements UnshatteredPacketBase {
    private static final String PAYLOAD_PATH = "open_storage";
    public static final Type<OpenStoragePacket> TYPE = UnshatteredPacketBase.type(PAYLOAD_PATH);

    public static final StreamCodec<RegistryFriendlyByteBuf, OpenStoragePacket> STREAM_CODEC = StreamCodec.unit(new OpenStoragePacket());

    @Override
    public String typeIdentifierPath() {
        return PAYLOAD_PATH;
    }
}
