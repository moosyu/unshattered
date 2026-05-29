package io.github.moosyu.events;

import io.github.moosyu.attributes.ModAttributes;
import io.github.moosyu.particles.DamageNumberParticle;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.network.PacketDistributor;

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
            if (player.getAttributeValue(ModAttributes.CRITICAL_CHANCE.holder) >= (Math.random() * 101)) critDamage = player.getAttributeValue(ModAttributes.CRITICAL_DAMAGE.holder);
            double damage = ((5 + player.getAttributeValue(ModAttributes.DAMAGE.holder)) * (1 + (player.getAttributeValue(ModAttributes.STRENGTH.holder) / 100))) * (1 + (critDamage / 100));
            AttributeInstance health = target.getAttribute(ModAttributes.HEALTH.holder);
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
                PacketDistributor.sendToPlayer((ServerPlayer) player, new DamageNumberData(damage, target.getX(), target.getY(), target.getZ()));
            }
            player.resetAttackStrengthTicker();
            if (sprinting) player.setSprinting(true);
        }
    }

    public record DamageNumberData(double damage, double targetPosX, double targetPosY, double targetPosZ) implements CustomPacketPayload {
        public static final CustomPacketPayload.Type<DamageNumberData> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("nno", "damage_number_data"));
        public static final StreamCodec<ByteBuf, DamageNumberData> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.DOUBLE,
                DamageNumberData::damage,
                ByteBufCodecs.DOUBLE,
                DamageNumberData::targetPosX,
                ByteBufCodecs.DOUBLE,
                DamageNumberData::targetPosY,
                ByteBufCodecs.DOUBLE,
                DamageNumberData::targetPosZ,
                DamageNumberData::new
        );

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {return TYPE;}
    }
}