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
import net.minecraft.world.item.ItemStack;
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
            if (player.level().isClientSide() || !(player instanceof ServerPlayer serverPlayer)) {
                return;
            }

            AbilityContext context = new AbilityContext().add(AbilityContextKey.PLAYER, serverPlayer)
                    .add(AbilityContextKey.DAMAGE_AMOUNT, (double) event.getAmount())
                    .add(AbilityContextKey.DAMAGE_SOURCE, source);
            try (AbilityScope scope = AbilityScope.open(serverPlayer, AbilityTriggerType.PLAYER_INCOMING_DAMAGE, context)) {
                if (scope.results.get(AbilityResult.CANCEL_EVENT) || source.is(DamageTypeTags.IS_FALL) && (int) (event.getAmount() + 3) < 7) {
                    event.setCanceled(true);
                    scope.close();
                    return;
                }
            }
        } else {
            if (!source.is(DamageTypeTags.IS_PLAYER_ATTACK)) {
                event.setCanceled(true);
            }

             if (source.getEntity() instanceof Player player && player.getAttributeValue(UnshatteredAttributeValues.FINAL_DAMAGE_MODIFIER.holder) == 0.0d) {
                player.sendSystemMessage(Component.translatable("combat.messages.unshattered.failed").withColor(UnshatteredUtils.RED));
            }
        }
    }
}
