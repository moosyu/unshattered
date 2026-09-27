package io.github.moosyu.mixins;

import com.google.common.collect.Lists;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.damage.DamageUtils;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin extends Projectile {
    @Shadow private double baseDamage;
    @Shadow @Nullable private IntOpenHashSet piercingIgnoreEntityIds;
    @Shadow private @Nullable List<Entity> piercedAndKilledEntities;
    @Shadow private ItemStack firedFromWeapon;
    @Shadow private SoundEvent soundEvent;
    @Shadow protected abstract void doKnockback(LivingEntity entity, DamageSource source);
    @Shadow protected abstract void doPostHurtEffects(LivingEntity entity);
    @Shadow protected abstract ItemStack getPickupItem();

    protected AbstractArrowMixin(EntityType<? extends Projectile> type, Level level) {
        super(type, level);
    }

    @ModifyConstant(method = "<init>", constant = @Constant(doubleValue = 2.0))
    private double setBaseDamage(double original) {
        if (getOwner() instanceof LivingEntity livingEntity) {
            return livingEntity.getAttributeValue(UnshatteredAttributeValues.DAMAGE.holder);
        } else {
            return original;
        }
    }

    @Inject(method = "onHitEntity", at = @At("HEAD"), cancellable = true)
    private void onHitEntity(EntityHitResult hitResult, CallbackInfo ci) {
        ci.cancel();

        Entity target = hitResult.getEntity();
        AbstractArrow arrow = (AbstractArrow) (Object)this;
        float pow = (float) getDeltaMovement().length();
        double arrowDamage = baseDamage;
        Entity currentOwner = getOwner();
        DamageSource damageSource = arrow.damageSources().arrow(arrow, (currentOwner != null ? currentOwner : arrow));
        if (getWeaponItem() != null && level() instanceof ServerLevel serverLevel) {
            arrowDamage = EnchantmentHelper.modifyDamage(serverLevel, getWeaponItem(), target, damageSource, (float)arrowDamage);
        }

        int damage = Mth.ceil(Mth.clamp((double)pow * arrowDamage, 0.0f, Integer.MAX_VALUE));
        if (arrow.getPierceLevel() > 0) {
            if (piercingIgnoreEntityIds == null) {
                piercingIgnoreEntityIds = new IntOpenHashSet(5);
            }

            if (piercedAndKilledEntities == null) {
                piercedAndKilledEntities = Lists.newArrayListWithCapacity(5);
            }

            if (piercingIgnoreEntityIds.size() >= arrow.getPierceLevel() + 1) {
                discard();
                return;
            }

            piercingIgnoreEntityIds.add(target.getId());
        }

        if (arrow.isCritArrow()) {
            long dmgIncrease = random.nextInt(damage / 2 + 2);
            damage = (int) Math.min(dmgIncrease + (long) damage, Integer.MAX_VALUE);
        }

        if (currentOwner instanceof LivingEntity livingOwner) {
            livingOwner.setLastHurtMob(target);
        }

        boolean isEnderman = target.is(EntityType.ENDERMAN);
        int remainingFireTicks = target.getRemainingFireTicks();
        if (isOnFire() && !isEnderman) {
            target.igniteForSeconds(5.0f);
        }

        if (isEnderman) return;

        if (target instanceof LivingEntity mob) {
            if (currentOwner instanceof Player player) {
                if (getWeaponItem() != null) {
                    DamageUtils.playerDealDamage(player, mob, damage, false, getWeaponItem());
                }

                if (!level().isClientSide() && arrow.getPierceLevel() <= 0) {
                    mob.setArrowCount(mob.getArrowCount() + 1);
                }

                doKnockback(mob, damageSource);
                doPostHurtEffects(mob);

                if (mob instanceof Player && currentOwner instanceof ServerPlayer ownerPlayer) {
                    if (!isSilent() && mob != ownerPlayer) {
                        ownerPlayer.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.PLAY_ARROW_HIT_SOUND, 0.0f));
                    }
                }

                if (!target.isAlive() && piercedAndKilledEntities != null) {
                    piercedAndKilledEntities.add(mob);
                }

                if (!level().isClientSide() && currentOwner instanceof ServerPlayer serverPlayer) {
                    if (piercedAndKilledEntities != null) {
                        CriteriaTriggers.KILLED_BY_ARROW.trigger(serverPlayer, piercedAndKilledEntities, firedFromWeapon);
                    } else if (!target.isAlive()) {
                        CriteriaTriggers.KILLED_BY_ARROW.trigger(serverPlayer, List.of(target), firedFromWeapon);
                    }
                }

                playSound(soundEvent, 1.0f, 1.2f / (this.random.nextFloat() * 0.2f + 0.9f));
            } else if (target instanceof Player targetPlayer && currentOwner instanceof LivingEntity livingOwner) {
                DamageUtils.damagePlayer(targetPlayer,
                        livingOwner.getAttributeValue(UnshatteredAttributeValues.DAMAGE.holder),
                        Component.literal("☠ " + targetPlayer.getPlainTextName() + " was shot to death by a " + livingOwner.getPlainTextName() + "!"),
                        false,
                        damageSource
                );

                Vec3 deltaMovement = arrow.getDeltaMovement();

                doPostHurtEffects(targetPlayer);
                targetPlayer.indicateDamage(deltaMovement.x, deltaMovement.z);

                // for whatever reason doKnockback does nothing here so i just did it by myself. do tell if youre reading this and know why.
                if (targetPlayer instanceof ServerPlayer serverPlayer) {
                    // needs to be negative so the knockback doesnt pull the player towards the direction of the x z vector
                    targetPlayer.knockback(0.4, -deltaMovement.x, -deltaMovement.z);
                    serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(targetPlayer));
                }

                // sounds close enough to vanilla's damage sound
                targetPlayer.level().playSound(null, targetPlayer.blockPosition(), SoundEvents.PLAYER_HURT, SoundSource.PLAYERS, 1.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f);
            }
            if (arrow.getPierceLevel() <= 0) {
                discard();
            }
        } else {
            target.setRemainingFireTicks(remainingFireTicks);
            deflect(ProjectileDeflection.REVERSE, target, owner, false);
            setDeltaMovement(getDeltaMovement().scale(0.2));
            Level level = arrow.level();
            if (level instanceof ServerLevel serverLevel) {
                if (getDeltaMovement().lengthSqr() < 1.0E-7) {
                    if (arrow.pickup == AbstractArrow.Pickup.ALLOWED) {
                        spawnAtLocation(serverLevel, getPickupItem(), 0.1f);
                    }

                    arrow.discard();
                }
            }
        }
    }
}