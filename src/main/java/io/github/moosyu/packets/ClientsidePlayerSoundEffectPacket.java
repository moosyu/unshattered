package io.github.moosyu.packets;

import io.github.moosyu.Unshattered;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import org.jspecify.annotations.NonNull;

public record ClientsidePlayerSoundEffectPacket(Holder<SoundEvent> soundEvent, float volume, float pitch) implements CustomPacketPayload {
    public ClientsidePlayerSoundEffectPacket(Holder<SoundEvent> soundEvent, float volume) {
        this(soundEvent, volume, 1.0f);
    }

    public static final Type<ClientsidePlayerSoundEffectPacket> TYPE = new Type<>(UnshatteredUtils.getUnshatteredIdentifier("clientside_sound_effect"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientsidePlayerSoundEffectPacket> STREAM_CODEC = StreamCodec.composite(
            SoundEvent.STREAM_CODEC, ClientsidePlayerSoundEffectPacket::soundEvent,
            ByteBufCodecs.FLOAT, ClientsidePlayerSoundEffectPacket::volume,
            ByteBufCodecs.FLOAT, ClientsidePlayerSoundEffectPacket::pitch,
            ClientsidePlayerSoundEffectPacket::new
    );

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
