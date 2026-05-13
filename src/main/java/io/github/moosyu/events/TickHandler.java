package io.github.moosyu.events;

import io.github.moosyu.helpers.ModHelpers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class TickHandler {
    static boolean fishApproaching = false;
    static boolean fishNibbling = false;

    @EventBusSubscriber(modid = "nno")
    public static class EventHandler {
        @SubscribeEvent
        public static void onPlayerTick(PlayerTickEvent.Post event) {
            Player player = event.getEntity();

            if (!(player.fishing instanceof FishingHook hook)) {
                return;
            }

            if (player.level().isClientSide()) {
                return;
            }

            boolean currentlyApproaching = hook.timeUntilHooked > 0;
            boolean currentlyNibbling = hook.nibble > 0;

            // I'll make the exclamation mark pop up above bobber once I get deep enough in that I know how to do that. For now it's just the basis here.
            if (currentlyApproaching && !fishApproaching) {
                ModHelpers.broadcastMessage("Fish approaching");
            } else if (currentlyNibbling && !fishNibbling) {
                ModHelpers.broadcastMessage("Nibbling");
            }

            fishApproaching = currentlyApproaching;
            fishNibbling = currentlyNibbling;
        }
    }
}
