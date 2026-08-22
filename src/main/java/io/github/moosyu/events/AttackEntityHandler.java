package io.github.moosyu.events;

import io.github.moosyu.items.UnshatteredPassiveAbilityItem;
import io.github.moosyu.util.AbilityUtils;
import io.github.moosyu.util.DamageUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class AttackEntityHandler {
    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        Player player = event.getEntity();
        if (!(event.getTarget() instanceof LivingEntity target) || player.level().isClientSide()) return;
        event.setCanceled(true);

        DamageUtil.dealDamage(player, target, AbilityUtils.triggerPassiveAbility(player,
                target,
                player.getItemInHand(InteractionHand.MAIN_HAND).getItem())
        );
    }
}