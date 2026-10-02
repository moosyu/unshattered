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

            boolean abilityCancelled = false;
            AbilityContext context = new AbilityContext().add(AbilityContextKey.PLAYER, player);
            for (ItemStack itemStack : abilities.getStoredNonOngoingItems()) {
                if (itemStack.getItem() instanceof PassiveAbilityItem passiveAbilityItem
                        && passiveAbilityItem.triggerTypes().contains(AbilityTriggerType.PLAYER_INCOMING_KNOCKBACK)
                        && passiveAbilityItem.abilityConditionsMet(context)
                ) {
                    if (passiveAbilityItem.triggerResult().map(it -> it instanceof AbilityTriggerResult result && result == AbilityTriggerResult.CANCEL_EVENT).orElse(false)) {
                        abilityCancelled = true;
                        break;
                    }
                }
            }

            if (state.isKnockbackCancelled() || abilityCancelled) {
                event.setCanceled(true);
            }

            state.setCancelledKnockback(false);
        }
    }
}
