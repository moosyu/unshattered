package io.github.moosyu.events;

import io.github.moosyu.attachments.PlayerSkillsAttachment;
import io.github.moosyu.attributes.UnshatteredAttributes;
import io.github.moosyu.data.components.SkillRequirement;
import io.github.moosyu.attachments.AttachmentRegistry;
import io.github.moosyu.data.components.DataComponentRegistry;
import io.github.moosyu.items.UnshatteredPassiveAbilityItem;
import io.github.moosyu.packets.DamageNumberPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.joml.*;

import java.lang.Math;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

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

        if (player.getMainHandItem().getItem() instanceof UnshatteredPassiveAbilityItem item) {
            if (item.abilityConditionsMet(player, target)) {
                item.onAbilityTriggered(player, target);
            }
        }

        double critDamage = 0.0d;
        if (player.getAttributeValue(UnshatteredAttributes.CRITICAL_CHANCE.holder) >= (Math.random() * 101)) critDamage = player.getAttributeValue(UnshatteredAttributes.CRITICAL_DAMAGE.holder);
        double damage = (((5 + player.getAttributeValue(UnshatteredAttributes.DAMAGE.holder)) * (1 + (player.getAttributeValue(UnshatteredAttributes.STRENGTH.holder) / 100))) * (1 + (critDamage / 100))) * player.getAttributeValue(UnshatteredAttributes.FINAL_DAMAGE_MODIFIER.holder);
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
            PacketDistributor.sendToPlayer((ServerPlayer) player, new DamageNumberPacket((int) damage, new Vector3fc() {
                @Override
                public float x() {
                    return 0;
                }

                @Override
                public float y() {
                    return 0;
                }

                @Override
                public float z() {
                    return 0;
                }

                @Override
                public FloatBuffer get(FloatBuffer buffer) {
                    return null;
                }

                @Override
                public FloatBuffer get(int index, FloatBuffer buffer) {
                    return null;
                }

                @Override
                public ByteBuffer get(ByteBuffer buffer) {
                    return null;
                }

                @Override
                public ByteBuffer get(int index, ByteBuffer buffer) {
                    return null;
                }

                @Override
                public Vector3fc getToAddress(long address) {
                    return null;
                }

                @Override
                public Vector3f sub(Vector3fc v, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f sub(float x, float y, float z, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f add(Vector3fc v, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f add(float x, float y, float z, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f fma(Vector3fc a, Vector3fc b, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f fma(float a, Vector3fc b, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f mulAdd(Vector3fc a, Vector3fc b, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f mulAdd(float a, Vector3fc b, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f mul(Vector3fc v, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f div(Vector3fc v, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f mulProject(Matrix4fc mat, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f mulProject(Matrix4fc mat, float w, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f mulProjectTranslation(Matrix4fc mat, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f mulProjectTranslation(Matrix4dc mat, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f mulProjectAffine(Matrix4fc mat, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f mulProjectAffine(Matrix4dc mat, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f mulProjectGeneric(Matrix4fc mat, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f mulProjectGeneric(Matrix4dc mat, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f mul(Matrix3fc mat, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f mul(Matrix3dc mat, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f mul(Matrix3x2fc mat, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f mul(Matrix3x2dc mat, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f mulTranspose(Matrix3fc mat, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f mulPosition(Matrix4fc mat, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f mulPosition(Matrix4dc mat, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f mulPosition(Matrix4x3fc mat, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f mulPosition(Matrix4x3dc mat, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f mulPositionGeneric(Matrix4fc mat, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f mulPositionGeneric(Matrix4dc mat, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f mulPositionTranslation(Matrix4fc mat, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f mulPositionTranslation(Matrix4dc mat, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f mulPositionTranslation(Matrix4x3fc mat, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f mulPositionTranslation(Matrix4x3dc mat, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f mulTransposePosition(Matrix4fc mat, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f mulTransposePosition(Matrix4dc mat, Vector3f dest) {
                    return null;
                }

                @Override
                public float mulPositionW(Matrix4fc mat, Vector3f dest) {
                    return 0;
                }

                @Override
                public float mulPositionW(Matrix4dc mat, Vector3f dest) {
                    return 0;
                }

                @Override
                public Vector3f mulDirection(Matrix4dc mat, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f mulDirection(Matrix4fc mat, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f mulDirection(Matrix4x3fc mat, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f mulDirection(Matrix4x3dc mat, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f mulTransposeDirection(Matrix4fc mat, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f mul(float scalar, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f mul(float x, float y, float z, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f div(float scalar, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f div(float x, float y, float z, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f rotate(Quaternionfc quat, Vector3f dest) {
                    return null;
                }

                @Override
                public Quaternionf rotationTo(Vector3fc toDir, Quaternionf dest) {
                    return null;
                }

                @Override
                public Quaternionf rotationTo(float toDirX, float toDirY, float toDirZ, Quaternionf dest) {
                    return null;
                }

                @Override
                public Vector3f rotateAxis(float angle, float aX, float aY, float aZ, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f rotateX(float angle, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f rotateY(float angle, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f rotateZ(float angle, Vector3f dest) {
                    return null;
                }

                @Override
                public float lengthSquared() {
                    return 0;
                }

                @Override
                public float length() {
                    return 0;
                }

                @Override
                public Vector3f normalize(Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f normalize(float length, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f cross(Vector3fc v, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f cross(float x, float y, float z, Vector3f dest) {
                    return null;
                }

                @Override
                public float distance(Vector3fc v) {
                    return 0;
                }

                @Override
                public float distance(float x, float y, float z) {
                    return 0;
                }

                @Override
                public float distanceSquared(Vector3fc v) {
                    return 0;
                }

                @Override
                public float distanceSquared(float x, float y, float z) {
                    return 0;
                }

                @Override
                public float dot(Vector3fc v) {
                    return 0;
                }

                @Override
                public float dot(float x, float y, float z) {
                    return 0;
                }

                @Override
                public float angleCos(Vector3fc v) {
                    return 0;
                }

                @Override
                public float angle(Vector3fc v) {
                    return 0;
                }

                @Override
                public float angleSigned(Vector3fc v, Vector3fc n) {
                    return 0;
                }

                @Override
                public float angleSigned(float x, float y, float z, float nx, float ny, float nz) {
                    return 0;
                }

                @Override
                public Vector3f min(Vector3fc v, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f max(Vector3fc v, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f negate(Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f absolute(Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f reflect(Vector3fc normal, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f reflect(float x, float y, float z, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f half(Vector3fc other, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f half(float x, float y, float z, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f smoothStep(Vector3fc v, float t, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f hermite(Vector3fc t0, Vector3fc v1, Vector3fc t1, float t, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f lerp(Vector3fc other, float t, Vector3f dest) {
                    return null;
                }

                @Override
                public float get(int component) throws IllegalArgumentException {
                    return 0;
                }

                @Override
                public Vector3i get(int mode, Vector3i dest) {
                    return null;
                }

                @Override
                public Vector3f get(Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3d get(Vector3d dest) {
                    return null;
                }

                @Override
                public int maxComponent() {
                    return 0;
                }

                @Override
                public int minComponent() {
                    return 0;
                }

                @Override
                public Vector3f orthogonalize(Vector3fc v, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f orthogonalizeUnit(Vector3fc v, Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f floor(Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f ceil(Vector3f dest) {
                    return null;
                }

                @Override
                public Vector3f round(Vector3f dest) {
                    return null;
                }

                @Override
                public boolean isFinite() {
                    return false;
                }

                @Override
                public boolean equals(Vector3fc v, float delta) {
                    return false;
                }

                @Override
                public boolean equals(float x, float y, float z) {
                    return false;
                }
            }));
        }
        // this seemed easier than transient modifiers that must be removed
        if (finalDamageAttribute != null) {
            finalDamageAttribute.setBaseValue(1);
        }
        player.resetAttackStrengthTicker();
        if (sprinting) player.setSprinting(true);
    }
}