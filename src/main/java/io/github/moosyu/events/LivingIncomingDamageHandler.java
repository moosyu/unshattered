package io.github.moosyu.events;

import io.github.moosyu.abilities.*;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

import java.util.ArrayList;
import java.util.List;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class LivingIncomingDamageHandler {
    @SubscribeEvent
    public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
        DamageSource source = event.getSource();

        if (event.getEntity() instanceof Player player) {
            if (player.level().isClientSide()) return;

            List<PassiveAbilityItem> triggeredItems = new ArrayList<>();
            AbilityContext context = new AbilityContext().add(AbilityContextKey.PLAYER, (ServerPlayer) player).add(AbilityContextKey.DAMAGE_AMOUNT, (double) event.getAmount());

            for (PassiveAbilityItem item : player.getData(UnshatteredAttachments.PLAYER_ABILITIES).getStoredPassiveNonOngoingItems()) {
                if (item.triggerTypes().contains(AbilityTriggerType.PLAYER_INCOMING_DAMAGE) && item.abilityConditionsMet(context)) {
                    item.onAbilityTriggered(context);
                    triggeredItems.add(item);
                    if (item.triggerResult().isPresent() && item.triggerResult().get() == AbilityTriggerResult.CANCEL_EVENT) {
                        triggeredItems.forEach(triggeredItem -> triggeredItem.onAbilityFinished(context));
                        event.setCanceled(true);
                    }
                }
            }

            if (source.is(DamageTypeTags.IS_FALL) && (int) (event.getAmount() + 3) < 7) {
                event.setCanceled(true);
            }

            triggeredItems.forEach(triggeredItem -> triggeredItem.onAbilityFinished(context));
        } else {
            if (!source.is(DamageTypeTags.IS_PLAYER_ATTACK)) {
                event.setCanceled(true);
            }

             if (source.getEntity() instanceof Player player && player.getAttributeValue(UnshatteredAttributeValues.FINAL_DAMAGE_MODIFIER.holder) == 0.0d) {
                player.sendSystemMessage(Component.translatable("combat.messages.unshattered.failed").withColor(UnshatteredUtils.ERROR_COLOR));
            }
        }
    }
}
