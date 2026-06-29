package io.github.moosyu.events;

import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.util.CheckItemRequirement;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class LivingIncomingDamageHandler {
    @SubscribeEvent
    public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
        DamageSource source = event.getSource();
        if (event.getEntity() instanceof Player) {
            if (source.is(DamageTypeTags.IS_FALL) && (int) (event.getAmount() + 3) < 7) {
                event.setCanceled(true);
            }
        } else {
            if (source.is(DamageTypeTags.IS_FALL)) {
                event.setCanceled(true);
            } else if (source.getEntity() instanceof Player player && player.getAttributeValue(UnshatteredAttributeValues.FINAL_DAMAGE_MODIFIER.holder) == 0.0d) {
                event.setCanceled(true);
                player.getAttribute(UnshatteredAttributeValues.FINAL_DAMAGE_MODIFIER.holder).setBaseValue(1.0d);
                player.sendSystemMessage(Component.translatable("combat.messages.unshattered.failed").withColor(CheckItemRequirement.ERROR_COLOR));
            }
        }
    }
}
