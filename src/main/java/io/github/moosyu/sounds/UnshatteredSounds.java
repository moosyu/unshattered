package io.github.moosyu.sounds;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import java.util.Random;

public class UnshatteredSounds {
    // stop forgetting this only works clientside
    public static void playerExperienceSound(Player player) {
        Random rand = new Random();
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.5f, 2.0f - rand.nextFloat(0.3f));
    }
    public static void playerDeathSound(Player player) {
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ANVIL_FALL, SoundSource.PLAYERS, 0.5f, 2.0f);
    }
}
