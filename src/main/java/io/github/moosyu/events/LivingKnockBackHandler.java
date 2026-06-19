package io.github.moosyu.events;

import io.github.moosyu.attachments.PlayerStateAttachment;
import io.github.moosyu.attachments.UnshatteredAttachments;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingKnockBackEvent;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class LivingKnockBackHandler {
    @SubscribeEvent
    public static void onLivingKnockBack(LivingKnockBackEvent event) {
        if (event.getEntity() instanceof Player player && !player.level().isClientSide()) {
            PlayerStateAttachment state = player.getData(UnshatteredAttachments.PLAYER_STATE.get());
            if (state.isKnockbackCancelled()) event.setCanceled(true);
            state.setCancelledKnockback(false);
        }
    }
}
