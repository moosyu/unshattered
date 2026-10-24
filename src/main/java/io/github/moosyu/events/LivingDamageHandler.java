package io.github.moosyu.events;

import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.damage.DamageUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import static io.github.moosyu.Unshattered.LOGGER;
import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class LivingDamageHandler {
    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent.Pre event) {
        if (event.getEntity() instanceof Player player && !player.level().isClientSide()) {
            float original = event.getOriginalDamage();
            float remaining = event.getNewDamage();
            event.setNewDamage(0.0f);

            if (remaining <= 0.0f) {
                return;
            }

            double scale = Math.min(1.0f, remaining / original);
            DamageSource damageSource = event.getSource();
            double playerDefenceValue = player.getAttributeValue(UnshatteredAttributeValues.DEFENCE.holder);
            String playerName = player.getName().getString();

            if (event.getSource().getEntity() instanceof LivingEntity entity) {
                AttributeInstance damageAttributeInstance = entity.getAttribute(UnshatteredAttributeValues.DAMAGE.holder);
                // in case the damage attribute was never defined
                if (damageAttributeInstance == null) {
                    LOGGER.error("A damage attribute wasn't defined for entity: {}", entity.getName().getString());
                    return;
                }

                double damageDealt = damageAttributeInstance.getValue()
                        * (1 - (playerDefenceValue / (playerDefenceValue + 50)))
                        * scale;
                DamageUtils.damagePlayer(player,
                        damageDealt,
                        Component.literal("☠ " + playerName + " was slain by a " + entity.getName().getString() + "!"),
                        false,
                        damageSource
                );
            } else if (damageSource.is(DamageTypeTags.IS_FALL)) {
                // https://old.reddit.com/r/HypixelSkyblock/comments/fvozn7/fall_damage_calculator/
                DamageUtils.damagePlayer(player,
                        ((int) original) * (50.0 / 33.0) / (1 + playerDefenceValue / 100.0) * scale,
                        Component.literal("☠ " + playerName + " fell to their death!"),
                        false,
                        damageSource
                );
            } else if (damageSource.is(DamageTypeTags.IS_DROWNING)) {
                DamageUtils.damagePlayer(player,
                        (original * 50 / 33) / ((playerDefenceValue / 100) + 1) * scale,
                        Component.literal("☠ " + playerName + " drowned!"),
                        false,
                        damageSource
                );
            } else if (damageSource.is(DamageTypeTags.IS_FIRE)) {
                double trueDefence = player.getAttributeBaseValue(UnshatteredAttributeValues.TRUE_DEFENCE.holder);
                DamageUtils.damagePlayer(player,
                        (100.0f / 33.0f) * (1 - (trueDefence / (trueDefence + 100))) * scale,
                        Component.literal("☠ " + playerName + " burnt to death!"),
                        false,
                        damageSource
                );
            } else if (damageSource.is(DamageTypeTags.IS_FREEZING)) {
                // something something freezing damage idk
            } else {
                LOGGER.error("a damage attribute wasn't defined for: {}", damageSource.getMsgId());
            }
        } else {
            if (event.getSource().is(DamageTypeTags.IS_FIRE)) {
                event.setNewDamage(0.0f);
                event.getEntity().invulnerableTime = 0;
            }
        }
    }
}