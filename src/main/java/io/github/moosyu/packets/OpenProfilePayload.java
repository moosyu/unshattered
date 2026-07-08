package io.github.moosyu.packets;

import io.github.moosyu.Unshattered;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public record OpenProfilePayload() implements CustomPacketPayload {
    public static final Type<OpenProfilePayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Unshattered.MODID, "open_profile"));
    public static final StreamCodec<RegistryFriendlyByteBuf, OpenProfilePayload> STREAM_CODEC = StreamCodec.unit(new OpenProfilePayload());

    @Override
    public CustomPacketPayload.@NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
