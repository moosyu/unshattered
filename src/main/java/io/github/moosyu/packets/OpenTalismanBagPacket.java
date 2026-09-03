package io.github.moosyu.packets;

import io.github.moosyu.Unshattered;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public record OpenTalismanBagPacket() implements CustomPacketPayload {
    public static final Type<OpenTalismanBagPacket> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Unshattered.MODID, "open_menu"));
    public static final StreamCodec<RegistryFriendlyByteBuf, OpenTalismanBagPacket> STREAM_CODEC = StreamCodec.unit(new OpenTalismanBagPacket());

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
