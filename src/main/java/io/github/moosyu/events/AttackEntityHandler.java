package io.github.moosyu.events;

import io.github.moosyu.attributes.UnshatteredAttributeValues;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.damage.DamageUtils.*;

@EventBusSubscriber(modid = MODID)
public class AttackEntityHandler {
    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        Player player = event.getEntity();
        if (!(event.getTarget() instanceof LivingEntity target)
                || player.level().isClientSide()
                || (event.getTarget() instanceof Player)
        ) return;
        event.setCanceled(true);

        playerDealDamage(player,
                target,
                player.getAttributeValue(UnshatteredAttributeValues.DAMAGE.holder) + BASE_DAMAGE,
                false,
                player.getItemInHand(InteractionHand.MAIN_HAND),
                true
        );
    }

    @SubscribeEvent
    public static void onReachedFerocityHits(ServerTickEvent.Post event) {
        SCHEDULED_FEROCITY_ATTACKS.removeIf(FerocityHit::tick);
    }
}