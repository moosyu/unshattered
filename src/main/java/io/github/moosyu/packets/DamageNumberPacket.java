package io.github.moosyu.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import static io.github.moosyu.Unshattered.MODID;

public record DamageNumberPacket(int number, Vector3fc pos) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<DamageNumberPacket> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(MODID, "damage_number_packet"));
    public static final StreamCodec<ByteBuf, DamageNumberPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            DamageNumberPacket::number,
            ByteBufCodecs.VECTOR3F,
            DamageNumberPacket::pos,
            DamageNumberPacket::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}