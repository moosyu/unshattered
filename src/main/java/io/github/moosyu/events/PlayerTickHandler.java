package io.github.moosyu.events;

import io.github.moosyu.attachments.PlayerStateAttachment;
import io.github.moosyu.attributes.ModAttributes;
import io.github.moosyu.registers.AttributesRegistry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.registers.AttachmentRegistry.PLAYER_STATE;

public class PlayerTickHandler {
    static boolean fishApproaching = false;
    static boolean fishNibbling = false;

    @EventBusSubscriber(modid = MODID)
    public static class EventHandler {
        @SubscribeEvent
        public static void onPlayerTick(PlayerTickEvent.Post event) {
            Player player = event.getEntity();
            if (player.level().isClientSide()) return;
            PlayerStateAttachment stats = player.getData(PLAYER_STATE.get());

            // disable hunger effects
            player.getFoodData().setFoodLevel(20);
            player.getFoodData().setSaturation(5.0f);
            player.getFoodData().setExhaustion(0.0f);

            double maxHealth = player.getAttribute(ModAttributes.HEALTH.holder).getValue();
            // heal every 2 seconds
            if (player.tickCount % 40 == 0) {
                double healthGained = (1.5 + maxHealth/100) * (player.getAttribute(ModAttributes.HEALTH_REGEN.holder).getValue()/100);
                stats.addCurrentStat(PlayerStateAttachment.Stat.HEALTH, healthGained, maxHealth);
                player.syncData(PLAYER_STATE);
            } else {
                // update health if attribute changed. itll already be updated on the healing tick though
                if (maxHealth < stats.getCurrentStat(PlayerStateAttachment.Stat.HEALTH)) stats.setCurrentStat(PlayerStateAttachment.Stat.HEALTH, maxHealth);
                player.syncData(PLAYER_STATE);
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
