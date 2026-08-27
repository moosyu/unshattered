package io.github.moosyu.packets;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

import static io.github.moosyu.Unshattered.MODID;

public record FerocityEffectPacket(int entityIdentifier, boolean playSound) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<FerocityEffectPacket> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(MODID, "ferocity_sound_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, FerocityEffectPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, FerocityEffectPacket::entityIdentifier,
            ByteBufCodecs.BOOL, FerocityEffectPacket::playSound,
            FerocityEffectPacket::new
    );

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
