package io.github.moosyu.events;

import io.github.moosyu.helpers.HealingManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import static io.github.moosyu.NNO.MODID;

public class PlayerTickHandler {
    static boolean fishApproaching = false;
    static boolean fishNibbling = false;

    @EventBusSubscriber(modid = MODID)
    public static class EventHandler {
        @SubscribeEvent
        public static void onPlayerTick(PlayerTickEvent.Post event) {
            Player player = event.getEntity();

            if (player.level().isClientSide()) {
                return;
            }
            // disable hunger effects
            player.getFoodData().setFoodLevel(20);
            player.getFoodData().setSaturation(5.0f);
            player.getFoodData().setExhaustion(0.0f);

            // heal every 2 seconds
            if (player.tickCount % 40 == 0) {
                float maxHealth = player.getMaxHealth();
                float currentHealth = player.getHealth();

                if (currentHealth < maxHealth) {
                    float amountToHeal = 1.5f + (maxHealth / 100f);
                    HealingManager.heal(player, amountToHeal);
                }
            }

            // fishing popup
            if (player.fishing instanceof FishingHook hook) {
                boolean currentlyApproaching = hook.timeUntilHooked > 0;
                boolean currentlyNibbling = hook.nibble > 0;

                // ill make the exclamation mark pop up above bobber once I get deep enough in that I know how to do that. For now it's just the basis here.
                if (currentlyApproaching && !fishApproaching) {
                    System.out.println("Fish approaching");
                } else if (currentlyNibbling && !fishNibbling) {
                    System.out.println("Nibbling");
                }

                fishApproaching = currentlyApproaching;
                fishNibbling = currentlyNibbling;
            }
        }
    }
}
