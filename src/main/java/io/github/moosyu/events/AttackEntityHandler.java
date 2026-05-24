package io.github.moosyu.events;

import io.github.moosyu.registers.AttributesRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;

import static io.github.moosyu.NNO.MODID;

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
            if (player.getAttributeValue(AttributesRegistry.CRITICAL_CHANCE) >= (Math.random() * 101)) critDamage = player.getAttributeValue(AttributesRegistry.CRITICAL_DAMAGE);
            double damage = ((5 + player.getAttributeValue(AttributesRegistry.DAMAGE)) * (1 + (player.getAttributeValue(AttributesRegistry.STRENGTH) / 100))) * (1 + (critDamage / 100));
            AttributeInstance health = target.getAttribute(AttributesRegistry.HEALTH);
            if (target.invulnerableTime <= 0 && health != null) {
                if ((health.getBaseValue() - damage) > 0) {
                    health.setBaseValue(health.getBaseValue() - damage);
                    // fake hit to trigger some of the effects which i cant be bothered replicating
                    target.hurt(target.damageSources().playerAttack(player), 0.0f);
                    // has to be placed after hurt as hurt sets its own invulnerability
                    target.invulnerableTime = 10;
                } else {
                    // this should one shot just about any vanilla mob to my knowledge
                    target.hurt(target.damageSources().playerAttack(player), 500.0f);
                }
            }

            player.resetAttackStrengthTicker();
            if (sprinting) player.setSprinting(true);
        }
    }
}