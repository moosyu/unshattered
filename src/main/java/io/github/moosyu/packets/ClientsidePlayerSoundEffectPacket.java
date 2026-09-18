package io.github.moosyu.packets;

import io.github.moosyu.Unshattered;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import org.jspecify.annotations.NonNull;

public record ClientsidePlayerSoundEffectPacket(Holder<SoundEvent> soundEvent, float volume) implements CustomPacketPayload {
    public static final Type<ClientsidePlayerSoundEffectPacket> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Unshattered.MODID, "clientside_sound_effect"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientsidePlayerSoundEffectPacket> STREAM_CODEC = StreamCodec.composite(
            SoundEvent.STREAM_CODEC, ClientsidePlayerSoundEffectPacket::soundEvent,
            ByteBufCodecs.FLOAT, ClientsidePlayerSoundEffectPacket::volume,
            ClientsidePlayerSoundEffectPacket::new
    );

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
