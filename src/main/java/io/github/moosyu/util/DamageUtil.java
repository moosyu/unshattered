package io.github.moosyu.util;

import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.attachments.PlayerSkillsAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.components.SkillRequirement;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.UnshatteredPassiveAbilityItem;
import io.github.moosyu.packets.DamageNumberPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nullable;

public final class DamageUtil {
    /**
     * @param damage the damage attribute of the player
     * @param strength the strength attribute of the player
     * @param critDamage the crit damage of the player, presumably found from getCritDamage or this will always be critting
     * @param finalDamageModifier the damage modifier attribute of the player
     * @return the amount of damage done by a player's attack
     */
    public static double getDamage(double damage, double strength, double critDamage, double finalDamageModifier) {
        return (5 + damage) * (1 + (strength / 100)) * (1 + (critDamage / 100)) * finalDamageModifier;
    }

    /**
     * @param critChance the player's crit chance
     * @param baseCritDamage the player's crit damage
     * @return the amount of critical damage added or 0 if a crit wasnt rolled
     */
    public static double getCritDamage(double critChance, double baseCritDamage) {
        return critChance >= (Math.random() * 101) ? baseCritDamage : 0.0d;
    }

    /**
     * runs custom unshattered damage code that factors in unshattered damage attributes and checks skill requirements
     * @param player player dealing damage
     * @param target target attempting to be damaged
     */
    public static void dealDamage(Player player, LivingEntity target, @Nullable UnshatteredPassiveAbilityItem item) {
        SkillRequirement skillRequirement = player.getItemInHand(InteractionHand.MAIN_HAND).get(UnshatteredDataComponents.SKILL_REQUIREMENT);
        PlayerSkillsAttachment playerSkill = player.getData(UnshatteredAttachments.PLAYER_SKILLS.get());
        if (skillRequirement != null && skillRequirement.level() > playerSkill.getLevel(playerSkill.getExp(skillRequirement.skill()))) {
            player.sendSystemMessage(Component.literal(Component.translatable(skillRequirement.skill().getTranslationKey()).getString() + " level " + skillRequirement.level() + " is required to use this weapon!").withColor(0xFFFF5555));
            return;
        }

        double critDamage = DamageUtil.getCritDamage(player.getAttributeValue(UnshatteredAttributeValues.CRITICAL_CHANCE.holder), player.getAttributeValue(UnshatteredAttributeValues.CRITICAL_DAMAGE.holder));
        double damage = DamageUtil.getDamage(
                player.getAttributeValue(UnshatteredAttributeValues.DAMAGE.holder),
                player.getAttributeValue(UnshatteredAttributeValues.STRENGTH.holder),
                critDamage,
                player.getAttributeValue(UnshatteredAttributeValues.FINAL_DAMAGE_MODIFIER.holder)
        );
        AttributeInstance targetHealth = target.getAttribute(UnshatteredAttributeValues.HEALTH.holder);
        if (target.invulnerableTime <= 0 && targetHealth != null && (player.isCreative() || !target.is(EntityType.ARMOR_STAND))) {
            if ((targetHealth.getBaseValue() - damage) > 0) {
                targetHealth.setBaseValue(targetHealth.getBaseValue() - damage);
                // fake hit to trigger some of the effects which i cant be bothered replicating
                target.hurtServer((ServerLevel) target.level(), target.damageSources().playerAttack(player), 0.0f);
                // has to be placed after hurt as hurt sets its own invulnerability
                target.invulnerableTime = 10;
            } else {
                targetHealth.setBaseValue(0.0);
                // this should one shot just about any vanilla mob to my knowledge (and actually calculating it wouldnt make sense as custom mobs ill make will have a base normal hp of like 1)
                target.hurtServer((ServerLevel) target.level(), target.damageSources().playerAttack(player), 500.0f);
            }
            PacketDistributor.sendToPlayer((ServerPlayer) player, new DamageNumberPacket((int) damage, target.position()));
        }
        if (item != null) {
            AbilityUtils.finishPassiveAbility(player, target, item);
        }
        player.resetAttackStrengthTicker();
        if (player.isSprinting()) player.setSprinting(true);
    }
}
