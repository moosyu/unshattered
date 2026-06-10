package io.github.moosyu.events;

import io.github.moosyu.attachments.PlayerStateAttachment;
import io.github.moosyu.attributes.UnshatteredAttributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.attachments.AttachmentRegistry.PLAYER_STATE;

@EventBusSubscriber(modid = MODID)
public class PlayerTickHandler {
    static boolean fishApproaching = false;
    static boolean fishNibbling = false;
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;
        PlayerStateAttachment state = player.getData(PLAYER_STATE.get());
        final double MAX_HEALTH_VALUE = player.getAttributeValue(UnshatteredAttributes.HEALTH.holder);
        final double HEALTH_REGEN_VALUE = player.getAttributeValue(UnshatteredAttributes.HEALTH_REGEN.holder);
        final double MAX_MANA_VALUE = player.getAttributeValue(UnshatteredAttributes.MANA.holder);

        // disable hunger effects
        player.getFoodData().setFoodLevel(20);
        player.getFoodData().setSaturation(5.0f);
        //player.getFoodData().setExhaustion(0.0f);

        // heal every 2 seconds
        if (player.tickCount % 40 == 0) {
            double healthGained = (1.5 + MAX_HEALTH_VALUE / 100) * (HEALTH_REGEN_VALUE / 100);
            double manaGained = MAX_MANA_VALUE * 0.04;
            state.addCurrentStat(PlayerStateAttachment.Stat.HEALTH, healthGained, MAX_HEALTH_VALUE);
            player.syncData(PLAYER_STATE);
            state.addCurrentStat(PlayerStateAttachment.Stat.MANA, manaGained, MAX_MANA_VALUE);
            player.syncData(PLAYER_STATE);
        } else {
            // update health if attribute changed. itll already be updated on the healing tick though
            if (MAX_HEALTH_VALUE < state.getCurrentStat(PlayerStateAttachment.Stat.HEALTH)) state.setCurrentStat(PlayerStateAttachment.Stat.HEALTH, MAX_HEALTH_VALUE);
            player.syncData(PLAYER_STATE);
        }
        state.decrementInvulnerableTime();

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
