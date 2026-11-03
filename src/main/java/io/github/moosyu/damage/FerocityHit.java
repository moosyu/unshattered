package io.github.moosyu.damage;

import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.packets.DamageNumberPacket;
import io.github.moosyu.packets.FerocityEffectPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

public class FerocityHit {
    private final LivingEntity target;
    private int waitTime;
    private final double damage;
    private final Player player;
    private final boolean playSound;

    public FerocityHit(LivingEntity target, int waitTime, double damage, Player player, boolean playSound) {
        this.target = target;
        this.waitTime = waitTime;
        this.damage = damage;
        this.player = player;
        this.playSound = playSound;
    }

    /**
     * @return true if the wait time has hit 0
     */
    public boolean tick() {
        waitTime--;

        if (waitTime <= 0) {
            AttributeInstance targetHealth = target.getAttribute(UnshatteredAttributeValues.CURRENT_HEALTH.holder);

            if (targetHealth != null && (player.isCreative() || !target.is(EntityType.ARMOR_STAND))) {
                if ((targetHealth.getBaseValue() - damage) > 0) {
                    targetHealth.setBaseValue(targetHealth.getBaseValue() - damage);
                    target.invulnerableTime = 0;
                } else {
                    targetHealth.setBaseValue(0.0);
                    target.hurtServer((ServerLevel) target.level(), target.damageSources().playerAttack(player), Integer.MAX_VALUE);
                }

                PacketDistributor.sendToPlayer((ServerPlayer) player, new FerocityEffectPacket(target.getId(), playSound));
                PacketDistributor.sendToPlayer((ServerPlayer) player, new DamageNumberPacket((int) damage, target.position()));
            }

            return true;
        }

        return false;
    }
}