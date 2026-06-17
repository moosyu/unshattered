package io.github.moosyu.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

import static io.github.moosyu.Unshattered.MODID;

public record DeathSoundEffectPacket() implements CustomPacketPayload {
    public static final Type<DeathSoundEffectPacket> TYPE = new Type<>(Identifier.fromNamespaceAndPath(MODID, "death_sound_effect_packet"));
    public static final StreamCodec<ByteBuf, DeathSoundEffectPacket> STREAM_CODEC = StreamCodec.unit(new DeathSoundEffectPacket());

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
