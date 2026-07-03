package io.github.moosyu.util;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class PlayClientsideSound {
    public static void playClientsideSound(Player player, SoundEvent soundEvent, SoundSource soundSource, float volume, float pitch) {
        Level level = player.level();
        if (level.isClientSide()) {
            level.playLocalSound(player.getX(), player.getY(), player.getZ(), soundEvent, soundSource, volume, pitch, false);
        }
    }

    public static void playClientsideSound(Player player, SoundEvent soundEvent, SoundSource soundSource, float volume) {
        Level level = player.level();
        if (level.isClientSide()) {
            level.playLocalSound(player.getX(), player.getY(), player.getZ(), soundEvent, soundSource, volume, 1.0f, false);
        }
    }

}
