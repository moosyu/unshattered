package io.github.moosyu.packets.handlers;

import io.github.moosyu.packets.WeakHitSoundEffectPacket;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class WeakHitSoundEffectHandler {
    public static void handleData(final WeakHitSoundEffectPacket data, final IPayloadContext context) {
        UnshatteredUtils.playClientsideSound(context.player(), SoundEvents.PLAYER_ATTACK_WEAK, SoundSource.PLAYERS, 1.0f);
    }
}
