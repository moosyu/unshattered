package io.github.moosyu.entities.projectiles;

import io.github.moosyu.items.ItemTypes;
import io.github.moosyu.damage.DamageUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

import javax.annotation.Nullable;

public class HomingBee extends Projectile {
    public static final double SPEED = 0.6d;
    private static final int MAX_LIFETIME = 100;
    private final @Nullable Player player;
    private final double damage;
    private final @Nullable LivingEntity target;

    public HomingBee(EntityType<? extends Projectile> type, Level level) {
        this(type, level, null, null, 0.0d);
    }

    public HomingBee(EntityType<? extends Projectile> type, Level level, @Nullable Player player, @Nullable LivingEntity target, double damage) {
        super(type, level);
        this.player = player;
        this.target = target;
        this.damage = damage;
        if (player != null) {
            setOwner(player);
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NonNull Builder builder) {}

    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide()) {
            if (tickCount > MAX_LIFETIME) {
                burst();
                return;
            }
            steerTowardsTarget();
            if (isRemoved()) return;
        }


        HitResult hit = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hit.getType() != HitResult.Type.MISS && isAlive()) {
            hitTargetOrDeflectSelf(hit);
        }
        if (isRemoved()) return;

        setPos(position().add(getDeltaMovement()));
        updateRotation();
    }

    private void steerTowardsTarget() {
        if (target == null || !canHitEntity(target)) {
            burst();
            return;
        }

        Vec3 toTarget = target.getBoundingBox().getCenter().subtract(position());
        double dist = toTarget.length();

        // blow up in radius to make sure it never gets stuck in a loop circling back again and again
        if (dist <= SPEED + 0.3d) {
            hitEntity(target);
            return;
        }

        Vec3 desired = toTarget.scale(1.0 / dist);
        Vec3 current = getDeltaMovement();
        Vec3 direction;

        if (current.lengthSqr() < 1.0E-6) {
            direction = desired;
        } else {
            Vec3 blended = current.normalize().lerp(desired, 0.6d);
            direction = blended.lengthSqr() < 1.0E-6 ? desired : blended.normalize();
        }

        // disable wobble as it gets closer so it doesnt get weird and miss a hitbox it should hit
        double wobble = 0.08 * Math.min(1.0, dist / 4.0);
        direction = direction.add(random.nextGaussian() * wobble, random.nextGaussian() * wobble, random.nextGaussian() * wobble).normalize();
        setDeltaMovement(direction.scale(SPEED));
    }

    @Override
    protected void updateRotation() {
        Vec3 movement = getDeltaMovement();
        if (movement.lengthSqr() < 1.0E-7) return;

        setYRot(Mth.rotLerp(0.5f, getYRot(), (float) (Mth.atan2(-movement.x, movement.z) * Mth.RAD_TO_DEG)));
        setXRot(Mth.rotLerp(0.5f, getXRot(), (float) (-Mth.atan2(movement.y, movement.horizontalDistance()) * Mth.RAD_TO_DEG)));
    }

    @Override
    protected boolean canHitEntity(@NonNull Entity entity) {
        return super.canHitEntity(entity) && entity instanceof LivingEntity && !(entity instanceof Player);
    }

    private void hitEntity(LivingEntity victim) {
        if (player != null) {
            DamageUtils.playerDealDamage(player, victim, ItemTypes.TALISMAN, damage, false);
        }
        burst();
    }

    @Override
    protected void onHitEntity(@NonNull EntityHitResult result) {
        super.onHitEntity(result);
        if (!level().isClientSide()) {
            if (result.getEntity() instanceof LivingEntity hit) {
                hitEntity(hit);
            } else {
                burst();
            }
        }
    }

    @Override
    protected void onHitBlock(@NonNull BlockHitResult result) {
        super.onHitBlock(result);
        if (!level().isClientSide()) {
            burst();
        }
    }

    private void burst() {
        if (!isRemoved() && level() instanceof ServerLevel server) {
            double x = getX(), y = getY() + 0.2, z = getZ();

            server.sendParticles(ParticleTypes.WAX_ON, x, y, z, 14, 0.15, 0.15, 0.15, 0.25);
            server.sendParticles(ParticleTypes.FALLING_NECTAR, x, y, z, 5, 0.25, 0.1, 0.25, 0.0);
            server.playSound(null, x, y, z, SoundEvents.BEE_STING, SoundSource.NEUTRAL, 0.5f, 1.4f);
        }
        discard();
    }
}