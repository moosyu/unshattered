package io.github.moosyu.events;

import io.github.moosyu.util.AbilityUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.Iterator;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.util.DamageUtil.*;

@EventBusSubscriber(modid = MODID)
public class AttackEntityHandler {
    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        Player player = event.getEntity();
        if (!(event.getTarget() instanceof LivingEntity target) || player.level().isClientSide()) return;
        event.setCanceled(true);

        dealDamage(player, target, AbilityUtils.triggerPassiveAbility(player,
                target,
                player.getItemInHand(InteractionHand.MAIN_HAND).getItem())
        );
    }

    @SubscribeEvent
    public static void onReachedFerocityHits(ServerTickEvent event) {
        Iterator<FerocityHit> iterator = SCHEDULED_FEROCITY_ATTACKS.iterator();

        while (iterator.hasNext()) {
            FerocityHit instance = iterator.next();

            if (instance.triggerTime() <= event.getServer().getTickCount()) {
                iterator.remove();
            }
        }
    }
}