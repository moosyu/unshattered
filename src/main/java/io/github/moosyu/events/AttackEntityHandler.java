package io.github.moosyu.events;

import io.github.moosyu.attributes.ModAttributes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;

import static io.github.moosyu.Unshattered.MODID;

public class AttackEntityHandler {
    @EventBusSubscriber(modid = MODID)
    public static class EventHandler {
        @SubscribeEvent
        public static void onAttackEntity(AttackEntityEvent event) {
            Player player = event.getEntity();
            boolean sprinting = player.isSprinting();
            if (!(event.getTarget() instanceof LivingEntity target) || player.level().isClientSide()) return;
            event.setCanceled(true);
            double critDamage = 0.0d;
            if (player.getAttributeValue(ModAttributes.CRITICAL_CHANCE.holder) >= (Math.random() * 101)) critDamage = player.getAttributeValue(ModAttributes.CRITICAL_DAMAGE.holder);
            double damage = ((5 + player.getAttributeValue(ModAttributes.DAMAGE.holder)) * (1 + (player.getAttributeValue(ModAttributes.STRENGTH.holder) / 100))) * (1 + (critDamage / 100));
            System.out.println(damage);
            AttributeInstance targetHealth = target.getAttribute(ModAttributes.HEALTH.holder);
            if (target.invulnerableTime <= 0 && targetHealth != null) {
                if ((targetHealth.getBaseValue() - damage) > 0) {
                    targetHealth.setBaseValue(targetHealth.getBaseValue() - damage);
                    // fake hit to trigger some of the effects which i cant be bothered replicating
                    target.hurtServer((ServerLevel) target.level(), target.damageSources().playerAttack(player), 0.0f);
                    // has to be placed after hurt as hurt sets its own invulnerability
                    target.invulnerableTime = 10;
                } else {
                    // this should one shot just about any vanilla mob to my knowledge
                    target.hurtServer((ServerLevel) target.level(), target.damageSources().playerAttack(player), 500.0f);
                }
            }
            player.resetAttackStrengthTicker();
            if (sprinting) player.setSprinting(true);
        }
    }
}