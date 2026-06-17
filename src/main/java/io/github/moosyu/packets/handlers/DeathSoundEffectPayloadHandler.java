package io.github.moosyu.packets.handlers;

import io.github.moosyu.packets.DeathSoundEffectPacket;
import io.github.moosyu.packets.ExpSoundEffectPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Random;

public class DeathSoundEffectPayloadHandler {
    public static void handleData(final DeathSoundEffectPacket data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            player.level().playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.ANVIL_LAND, SoundSource.PLAYERS, 1.0f, 2.0f, false);
        });
    }
}
