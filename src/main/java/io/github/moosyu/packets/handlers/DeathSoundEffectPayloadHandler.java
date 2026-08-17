package io.github.moosyu.packets.handlers;

import io.github.moosyu.packets.DeathSoundEffectPacket;
import io.github.moosyu.util.PlayClientsideSound;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class DeathSoundEffectPayloadHandler {
    public static void handleData(final DeathSoundEffectPacket data, final IPayloadContext context) {
        context.enqueueWork(() -> ClientHandler.handle(data, context));
    }

    @OnlyIn(Dist.CLIENT)
    private static class ClientHandler {
        static void handle(final DeathSoundEffectPacket data, final IPayloadContext context) {
            Player player = context.player();
            PlayClientsideSound.playClientsideSound(player, SoundEvents.ANVIL_LAND, SoundSource.PLAYERS, 1.0f, 2.0f);
        }
    }

}
