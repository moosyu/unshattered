package io.github.moosyu.entities.mobs;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

import java.util.EnumSet;

public abstract class UnshatteredSlime extends Mob {
    private final float damage;

    protected UnshatteredSlime(EntityType<? extends Mob> type, Level level, float damage) {
        super(type, level);

        this.damage = damage;
        moveControl = new SlimeMoveControl(this);
    }

    @Override
    public @NonNull SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }

    @Override
    protected @NonNull SoundEvent getHurtSound(@NonNull DamageSource source) {
        return SoundEvents.SLIME_HURT;
    }

    @Override
    public void playerTouch(@NonNull Player player) {
        if (level() instanceof ServerLevel level) {
            if (isAlive() && isWithinMeleeAttackRange(player) && hasLineOfSight(player)) {
                DamageSource damageSource = damageSources().mobAttack(this);
                if (player.hurtServer(level, damageSource, damage)) {
                    float knockback = getKnockback(player, damageSource);
                    if (knockback > 0.0f) {
                        double dx = Math.sin(getYRot() * Mth.DEG_TO_RAD);
                        double dz = -Math.cos(getYRot() * Mth.DEG_TO_RAD);
                        player.knockback(knockback * 0.5f, dx, dz);
                    }
                    playSound(SoundEvents.SLIME_ATTACK, 1.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f);
                    EnchantmentHelper.doPostAttackEffects(level, player, damageSource);
                }
            }
        }
    }

    @Override
    public void jumpFromGround() {
        Vec3 movement = getDeltaMovement();
        setDeltaMovement(movement.x, getJumpPower(), movement.z);
        needsSync = true;
    }

    protected int getJumpDelay() {
        return random.nextInt(20) + 10;
    }

    private float getSoundPitch() {
        return ((random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f) * 0.8f;
    }

    protected static class SlimeFloatGoal extends Goal {
        private final UnshatteredSlime slime;

        public SlimeFloatGoal(UnshatteredSlime mob) {
            slime = mob;
            setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
            mob.getNavigation().setCanFloat(true);
        }

        public boolean canUse() {
            return (slime.isInWater() || slime.isInLava()) && slime.getMoveControl() instanceof SlimeMoveControl;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            if (slime.getRandom().nextFloat() < 0.8f) {
                slime.getJumpControl().jump();
            }

            if (slime.getMoveControl() instanceof SlimeMoveControl slimeMoveControl) {
                slimeMoveControl.setWantedMovement(1.2);
            }
        }
    }

    protected static class SlimeKeepOnJumpingGoal extends Goal {
        private final UnshatteredSlime slime;

        public SlimeKeepOnJumpingGoal(UnshatteredSlime mob) {
            slime = mob;
            setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
        }

        public boolean canUse() {
            return !slime.isPassenger();
        }

        public void tick() {
            if (slime.getMoveControl() instanceof SlimeMoveControl slimeMoveControl) {
                slimeMoveControl.setWantedMovement(1.0f);
            }
        }
    }

    protected static class SlimeMoveControl extends MoveControl {
        private float yRot;
        private int jumpDelay;
        private final UnshatteredSlime slime;

        public SlimeMoveControl(UnshatteredSlime slime) {
            super(slime);
            this.slime = slime;
            yRot = 180.0f * slime.getYRot() / (float)Math.PI;
        }

        public void setDirection(float yRot) {
            this.yRot = yRot;
        }

        public void setWantedMovement(double speedModifier) {
            this.speedModifier = speedModifier;
            operation = Operation.MOVE_TO;
        }

        public void tick() {
            mob.setYRot(rotlerp(mob.getYRot(), yRot, 90.0f));
            mob.yHeadRot = mob.getYRot();
            mob.yBodyRot = mob.getYRot();
            if (operation != Operation.MOVE_TO) {
                mob.setZza(0.0f);
            } else {
                operation = Operation.WAIT;
                if (mob.onGround()) {
                    mob.setSpeed((float)(speedModifier * mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
                    if (jumpDelay-- <= 0) {
                        jumpDelay = slime.getJumpDelay();
                        jumpDelay /= 3;

                        slime.getJumpControl().jump();
                        slime.playSound(SoundEvents.SLIME_JUMP, slime.getSoundVolume(), slime.getSoundPitch());
                    } else {
                        slime.xxa = 0.0f;
                        slime.zza = 0.0f;
                        mob.setSpeed(0.0f);
                    }
                } else {
                    mob.setSpeed((float)(speedModifier * mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
                }
            }

        }
    }

    protected static class SlimeRandomDirectionGoal extends Goal {
        private final UnshatteredSlime slime;
        private float chosenDegrees;
        private int nextRandomizeTime;

        public SlimeRandomDirectionGoal(UnshatteredSlime slime) {
            this.slime = slime;
            setFlags(EnumSet.of(Flag.LOOK));
        }

        public boolean canUse() {
            return slime.getTarget() == null && (slime.onGround() || slime.isInWater() || slime.isInLava() || slime.hasEffect(MobEffects.LEVITATION)) && slime.getMoveControl() instanceof SlimeMoveControl;
        }

        public void tick() {
            if (--nextRandomizeTime <= 0) {
                nextRandomizeTime = adjustedTickDelay(40 + slime.getRandom().nextInt(60));
                chosenDegrees = (float)slime.getRandom().nextInt(360);
            }

            if (slime.getMoveControl() instanceof SlimeMoveControl slimeMoveControl) {
                slimeMoveControl.setDirection(chosenDegrees);
            }
        }
    }

    protected static class SlimeAttackGoal extends Goal {
        private final UnshatteredSlime slime;
        private int growTiredTimer;

        public SlimeAttackGoal(UnshatteredSlime slime) {
            this.slime = slime;
            setFlags(EnumSet.of(Flag.LOOK));
        }

        public boolean canUse() {
            LivingEntity target = slime.getTarget();
            if (target == null) {
                return false;
            } else {
                return slime.canAttack(target) && slime.getMoveControl() instanceof SlimeMoveControl;
            }
        }

        public void start() {
            growTiredTimer = reducedTickDelay(300);
            super.start();
        }

        public boolean canContinueToUse() {
            LivingEntity target = slime.getTarget();
            if (target == null) {
                return false;
            } else {
                return slime.canAttack(target) && --growTiredTimer > 0;
            }
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity target = slime.getTarget();
            if (target != null) {
                slime.lookAt(target, 10.0F, 10.0F);
            }

            if (slime.getMoveControl() instanceof UnshatteredSlime.SlimeMoveControl slimeMoveControl) {
                slimeMoveControl.setDirection(slime.getYRot());
            }
        }
    }
}