package io.github.moosyu.events;

import net.minecraft.world.entity.LivingEntity;
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
            if (!(event.getTarget() instanceof LivingEntity target))
                return;

            event.setCanceled(true);

            if (target.invulnerableTime <= 0) {
                float damage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
                if (target.hurt(player.damageSources().playerAttack(player), damage)) {
                    target.invulnerableTime = 10;
                }
            }

            player.resetAttackStrengthTicker();
            if (sprinting) player.setSprinting(true);
        }
    }
}