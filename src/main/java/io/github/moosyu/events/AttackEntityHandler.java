package io.github.moosyu.events;

import io.github.moosyu.attachments.PlayerSkillsAttachment;
import io.github.moosyu.attributes.UnshatteredAttributes;
import io.github.moosyu.data.components.SkillRequirement;
import io.github.moosyu.attachments.AttachmentRegistry;
import io.github.moosyu.data.components.DataComponentRegistry;
import io.github.moosyu.items.UnshatteredPassiveAbilitySword;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
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
        boolean sprinting = player.isSprinting();
        if (!(event.getTarget() instanceof LivingEntity target) || player.level().isClientSide()) return;
        event.setCanceled(true);
        SkillRequirement skillRequirement = player.getItemInHand(InteractionHand.MAIN_HAND).get(DataComponentRegistry.SKILL_REQUIREMENT);
        PlayerSkillsAttachment playerSkill = player.getData(AttachmentRegistry.PLAYER_SKILLS.get());
        AttributeInstance finalDamageAttribute = player.getAttribute(UnshatteredAttributes.FINAL_DAMAGE_MODIFIER.holder);
        if (skillRequirement != null && skillRequirement.level() > playerSkill.getLevel(playerSkill.getExp(skillRequirement.skill()))) {
            player.sendSystemMessage(Component.literal(skillRequirement.skill().getName() + " level " + skillRequirement.level() + " is required to use this weapon!").withColor(0xFFFF5555));
            return;
        }

        if (player.getMainHandItem().getItem() instanceof UnshatteredPassiveAbilitySword sword) {
            if (sword.abilityConditionsMet(player, target)) {
                sword.onAbilityTriggered(player, target);
            }
        }

        double critDamage = 0.0d;
        if (player.getAttributeValue(UnshatteredAttributes.CRITICAL_CHANCE.holder) >= (Math.random() * 101)) critDamage = player.getAttributeValue(UnshatteredAttributes.CRITICAL_DAMAGE.holder);
        double damage = (((5 + player.getAttributeValue(UnshatteredAttributes.DAMAGE.holder)) * (1 + (player.getAttributeValue(UnshatteredAttributes.STRENGTH.holder) / 100))) * (1 + (critDamage / 100))) * player.getAttributeValue(UnshatteredAttributes.FINAL_DAMAGE_MODIFIER.holder);
        System.out.println("damage dealt: " + damage);
        AttributeInstance targetHealth = target.getAttribute(UnshatteredAttributes.HEALTH.holder);
        if (target.invulnerableTime <= 0 && targetHealth != null) {
            if ((targetHealth.getBaseValue() - damage) > 0) {
                targetHealth.setBaseValue(targetHealth.getBaseValue() - damage);
                // fake hit to trigger some of the effects which i cant be bothered replicating
                target.hurtServer((ServerLevel) target.level(), target.damageSources().playerAttack(player), 0.0f);
                // has to be placed after hurt as hurt sets its own invulnerability

                target.invulnerableTime = 10;
            } else {
                // this should one shot just about any vanilla mob to my knowledge (and actually calculating it wouldnt make sense as custom mobs ill make will have a base normal hp of like 1)
                target.hurtServer((ServerLevel) target.level(), target.damageSources().playerAttack(player), 500.0f);
            }
        }
        // this seemed easier than transient modifiers that must be removed
        if (finalDamageAttribute != null) {
            finalDamageAttribute.setBaseValue(1);
        }
        player.resetAttackStrengthTicker();
        if (sprinting) player.setSprinting(true);
    }
}