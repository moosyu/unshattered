package io.github.moosyu.packets.handlers;

import io.github.moosyu.packets.ExpSoundEffectPacket;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Random;

public class ExpSoundEffectPayloadHandler {
    public static void handleData(final ExpSoundEffectPacket data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            Random rand = new Random();
            UnshatteredUtils.playClientsideSound(player, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.5f, 2.0f - rand.nextFloat(0.3f));
        });
    }
}
