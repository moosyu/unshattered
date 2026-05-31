package io.github.moosyu.events;

import io.github.moosyu.attachments.PlayerStateAttachment;
import io.github.moosyu.registers.AttachmentRegistry;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingKnockBackEvent;

import static io.github.moosyu.Unshattered.MODID;

public class LivingKnockBackHandler {
    @EventBusSubscriber(modid = MODID)
    public static class EventHandler {
        @SubscribeEvent
        public static void onLivingKnockBack(LivingKnockBackEvent event) {
            if (event.getEntity() instanceof Player player && !player.level().isClientSide()) {
                PlayerStateAttachment state = player.getData(AttachmentRegistry.PLAYER_STATE.get());
                if (state.shouldCancelKnockback()) event.setCanceled(true);
                state.setCancelledKnockback(false);
            }
        }
    }
}
