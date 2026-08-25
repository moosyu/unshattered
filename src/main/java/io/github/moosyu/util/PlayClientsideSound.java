package io.github.moosyu.util;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public final class PlayClientsideSound {
    /**
     * @param player the player having the sound played
     * @param soundEvent the sound event of the sound (usually taken from SoundEvents)
     * @param soundSource the source of the sound for volume settings
     * @param volume the volume of the sound
     * @param pitch the pitch of the sound
     */
    public static void playClientsideSound(Player player, SoundEvent soundEvent, SoundSource soundSource, float volume, float pitch) {
        Level level = player.level();
        if (level.isClientSide()) {
            level.playLocalSound(player.getX(), player.getY(), player.getZ(), soundEvent, soundSource, volume, pitch, false);
        }
    }

    /**
     * @param player the player having the sound played
     * @param soundEvent the sound event of the sound (usually taken from SoundEvents)
     * @param soundSource the source of the sound for volume settings
     * @param volume the volume of the sound
     */
    public static void playClientsideSound(Player player, SoundEvent soundEvent, SoundSource soundSource, float volume) {
        Level level = player.level();
        if (level.isClientSide()) {
            level.playLocalSound(player.getX(), player.getY(), player.getZ(), soundEvent, soundSource, volume, 1.0f, false);
        }
    }

}
