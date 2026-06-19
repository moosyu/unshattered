package io.github.moosyu.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import static io.github.moosyu.Unshattered.MODID;

public record DamageNumberPacket(int number, Vec3 pos) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<DamageNumberPacket> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(MODID, "damage_number_packet"));
    public static final StreamCodec<ByteBuf, DamageNumberPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            DamageNumberPacket::number,
            ByteBufCodecs.DOUBLE,
            DamageNumberPacket::getX,
            ByteBufCodecs.DOUBLE,
            DamageNumberPacket::getY,
            ByteBufCodecs.DOUBLE,
            DamageNumberPacket::getZ,
            (number, x, y, z) -> new DamageNumberPacket(number, new Vec3(x, y, z))
    );

    public double getX() { return pos.x; }
    public double getY() { return pos.y; }
    public double getZ() { return pos.z; }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}