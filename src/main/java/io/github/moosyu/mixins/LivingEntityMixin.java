package io.github.moosyu.mixins;

import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.client.particle.FireworkParticles;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow protected ItemStack useItem;
    @Shadow protected int useItemRemaining;

    @Inject(method = "applyItemBlocking", at = @At("HEAD"), cancellable = true)
    private void timedBlock(ServerLevel level, DamageSource source, float damage, CallbackInfoReturnable<Float> cir) {
        if (!((Object) this instanceof ServerPlayer player) || damage <= 0.0f) {
            return;
        }

        ItemStack shield = player.getItemBlockingWith();
        if (shield == null) {
            return;
        }

        BlocksAttacks blocksAttacks = shield.get(DataComponents.BLOCKS_ATTACKS);
        if (blocksAttacks == null) {
            return;
        }

        Vec3 sourcePosition = source.getSourcePosition();
        double angle;
        if (sourcePosition == null) {
            angle = Math.PI;
        } else {
            Vec3 vectorTo = sourcePosition.subtract(player.position());
            vectorTo = new Vec3(vectorTo.x, 0.0d, vectorTo.z).normalize();
            angle = Math.acos(vectorTo.dot(player.calculateViewVector(0.0f, player.getYHeadRot())));
        }

        float blockable = blocksAttacks.resolveBlockedDamage(source, damage, angle);
        if (blockable <= 0.0f) {
            cir.setReturnValue(0.0f);
            return;
        }

        if (player.getTicksUsingItem() <= 15) {
            this.useItemRemaining = this.useItem.getUseDuration(player);
            Vec3 pos = player.getEyePosition().add(player.getLookAngle().scale(0.6d));
            level.sendParticles(new DustParticleOptions(UnshatteredUtils.CYAN, 1.0f),
                    pos.x,
                    pos.y,
                    pos.z,
                    10,
                    0.2d,
                    0.2d,
                    0.2d,
                    0.15d
            );
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 0.8f, 1.6f);
            cir.setReturnValue(damage);
        } else {
            player.getCooldowns().addCooldown(shield, 10);
            player.stopUsingItem();

            if (sourcePosition != null) {
                player.knockback(Math.max(0.4f, damage / 50.0f), sourcePosition.x - player.getX(), sourcePosition.z - player.getZ());
                player.hurtMarked = true;
            }

            cir.setReturnValue(blockable * 0.9f);
        }
    }
}