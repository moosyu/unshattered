package io.github.moosyu.packets;

import io.github.moosyu.damage.DamageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

import static io.github.moosyu.Unshattered.MODID;

public record DamageNumberPacket(int number, Vec3 pos, DamageType damageType) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<DamageNumberPacket> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(MODID, "damage_number_packet"));
    public static final StreamCodec<FriendlyByteBuf, DamageNumberPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, DamageNumberPacket::number,
            ByteBufCodecs.DOUBLE, damageNumberPacket -> damageNumberPacket.pos().x,
            ByteBufCodecs.DOUBLE, damageNumberPacket -> damageNumberPacket.pos().y,
            ByteBufCodecs.DOUBLE, damageNumberPacket -> damageNumberPacket.pos().z,
            DamageType.STREAM_CODEC, DamageNumberPacket::damageType,
            (number, x, y, z, criticalHit) -> new DamageNumberPacket(number, new Vec3(x, y, z), criticalHit)
    );

    @Override
    public CustomPacketPayload.@NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}