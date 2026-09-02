package io.github.moosyu.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

import static io.github.moosyu.Unshattered.MODID;

public record WeakHitSoundEffectPacket() implements CustomPacketPayload {
    public static final Type<WeakHitSoundEffectPacket> TYPE = new Type<>(Identifier.fromNamespaceAndPath(MODID, "weak_hit_sound_effect"));
    public static final StreamCodec<ByteBuf, WeakHitSoundEffectPacket> STREAM_CODEC = StreamCodec.unit(new WeakHitSoundEffectPacket());

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
