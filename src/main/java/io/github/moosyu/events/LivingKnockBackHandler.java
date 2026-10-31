package io.github.moosyu.events;

import io.github.moosyu.abilities.*;
import io.github.moosyu.data.attachments.PlayerAbilityEffectsAttachment;
import io.github.moosyu.data.attachments.PlayerStateAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingKnockBackEvent;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class LivingKnockBackHandler {
    @SubscribeEvent
    public static void onLivingKnockBack(LivingKnockBackEvent event) {
        if (event.getEntity() instanceof ServerPlayer player && !player.level().isClientSide()) {
            PlayerStateAttachment state = player.getData(UnshatteredAttachments.PLAYER_STATE.get());
            PlayerAbilityEffectsAttachment abilities = player.getData(UnshatteredAttachments.PLAYER_ABILITIES.get());

            AbilityContext context = new AbilityContext().add(AbilityContextKey.PLAYER, player);
            try (AbilityScope scope = AbilityScope.open(player, AbilityTriggerType.PLAYER_INCOMING_KNOCKBACK, context)) {
                if (scope.results.get(AbilityResult.CANCEL_EVENT)) {
                    event.setCanceled(true);
                    scope.close();
                    return;
                }
            }

            if (state.isKnockbackCancelled()) {
                event.setCanceled(true);
            }

            state.setCancelledKnockback(false);
        }
    }
}
