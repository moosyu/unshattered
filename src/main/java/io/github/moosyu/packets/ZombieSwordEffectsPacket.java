package io.github.moosyu.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

import static io.github.moosyu.Unshattered.MODID;

public record ZombieSwordEffectsPacket() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ZombieSwordEffectsPacket> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(MODID, "zombie_sword_effect_packets"));
    public static final StreamCodec<ByteBuf, ZombieSwordEffectsPacket> STREAM_CODEC = StreamCodec.unit(new ZombieSwordEffectsPacket());

    @Override
    public CustomPacketPayload.@NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
