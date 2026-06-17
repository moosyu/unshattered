package io.github.moosyu.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

import static io.github.moosyu.Unshattered.MODID;

public record ExpSoundEffectPacket() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ExpSoundEffectPacket> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(MODID, "exp_effect_packet"));
    public static final StreamCodec<ByteBuf, ExpSoundEffectPacket> STREAM_CODEC = StreamCodec.unit(new ExpSoundEffectPacket());

    @Override
    public CustomPacketPayload.@NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
