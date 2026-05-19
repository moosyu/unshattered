package io.github.moosyu.sounds;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import java.util.Random;

public class ModSounds {
    // stop forgetting this only works clientside
    public static void playerExperienceSound(Player player) {
        Random rand = new Random();
        // not 100% about this just yet. slightly randomising the pitch helped, but I may add some random delay to the pitch sometimes to emulate
        // hypixel's shitting 5tps servers?
        player.playNotifySound(SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.NEUTRAL, 0.5f, 2.0f - rand.nextFloat(0.3f));
    }

    public static void playerDeathSound(Player player) {
        player.playNotifySound(SoundEvents.ANVIL_FALL, SoundSource.NEUTRAL, 0.5f, 2.0f);
    }
}
