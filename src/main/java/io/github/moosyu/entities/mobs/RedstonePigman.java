package io.github.moosyu.entities.mobs;

import io.github.moosyu.items.UnshatteredItems;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.EnumSet;

public class RedstonePigman extends Monster implements NeutralMob {
    // whether in this session the pigman has either alerted others or been alerted
    private boolean hasMadeInteraction = false;
    private long persistentAngerEndTime;
    private @Nullable EntityReference<LivingEntity> persistentAngerTarget;

    public RedstonePigman(EntityType<? extends RedstonePigman> type, Level level) {
        super(type, level);

        setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(UnshatteredItems.REDSTONE_SWORD.get()));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return UnshatteredUtils.createHostileMobAttributes(35.0f, 2.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.23f);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new RedstonePigmanGatherGoal(this));
        goalSelector.addGoal(2, new RedstonePigmanCryGoal(this));
        goalSelector.addGoal(3, new RedstonePigmanAttackGoal(this, 1.0f, true));
        goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0f));
        goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public long getPersistentAngerEndTime() {
        return persistentAngerEndTime;
    }

    @Override
    public void setPersistentAngerEndTime(long time) {
        persistentAngerEndTime = time;
    }

    @Override
    public @Nullable EntityReference<LivingEntity> getPersistentAngerTarget() {
        return persistentAngerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable EntityReference<LivingEntity> entityReference) {
        persistentAngerTarget = entityReference;
    }

    @Override
    public void startPersistentAngerTimer() {
        setTimeToRemainAngry(600);
    }

    @Override
    protected void addAdditionalSaveData(@NonNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        addPersistentAngerSaveData(output);
    }

    @Override
    protected void readAdditionalSaveData(@NonNull ValueInput input) {
        super.readAdditionalSaveData(input);
        readPersistentAngerSaveData(level(), input);
    }

    @Override
    protected @NonNull SoundEvent getHurtSound(@NonNull DamageSource source) {
        return SoundEvents.ZOMBIFIED_PIGLIN_HURT;
    }

    @Override
    protected @NonNull SoundEvent getDeathSound() {
        return SoundEvents.ZOMBIFIED_PIGLIN_DEATH;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return isAngry() ? SoundEvents.ZOMBIFIED_PIGLIN_ANGRY : SoundEvents.ZOMBIFIED_PIGLIN_AMBIENT;
    }

    private static class RedstonePigmanCryGoal extends Goal {
        private final RedstonePigman redstonePigman;
        private int cryTime;

        public RedstonePigmanCryGoal(RedstonePigman redstonePigman) {
            this.redstonePigman = redstonePigman;
            setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
        }

        @Override
        public boolean canUse() {
            return !redstonePigman.hasMadeInteraction && RedstonePigmenManager.targetPos == null && redstonePigman.getTarget() != null;
        }

        @Override
        public boolean canContinueToUse() {
            return cryTime > 0;
        }

        @Override
        public void start() {
            cryTime = 200;
            redstonePigman.getNavigation().stop();
            redstonePigman.playSound(SoundEvents.PIGLIN_ANGRY, 1.5f, 0.6f);
            if (redstonePigman.level() instanceof ServerLevel serverLevel && !serverLevel.isClientSide()) {
                RedstonePigmenManager.setTarget(serverLevel, redstonePigman.position());
            }
        }

        @Override
        public void tick() {
            cryTime--;
        }

        @Override
        public void stop() {
            if (cryTime <= 0) {
                redstonePigman.hasMadeInteraction = true;
                redstonePigman.setAggressive(true);
            }
        }
    }

    private static class RedstonePigmanAttackGoal extends MeleeAttackGoal {
        private final RedstonePigman redstonePigman;
        private int raiseArmTicks;

        public RedstonePigmanAttackGoal(RedstonePigman redstonePigman, double speedModifier, boolean trackTarget) {
            super(redstonePigman, speedModifier, trackTarget);
            this.redstonePigman = redstonePigman;
        }

        public void start() {
            super.start();
            raiseArmTicks = 0;
        }

        public void stop() {
            super.stop();
            redstonePigman.setAggressive(false);
        }

        public void tick() {
            super.tick();
            ++raiseArmTicks;
            redstonePigman.setAggressive(raiseArmTicks >= 5 && getTicksUntilNextAttack() < getAttackInterval() / 2);
        }
    }

    private static class RedstonePigmanGatherGoal extends Goal {
        private final RedstonePigman redstonePigman;

        public RedstonePigmanGatherGoal(RedstonePigman redstonePigman) {
            this.redstonePigman = redstonePigman;
            setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (redstonePigman.getTarget() != null) {
                return false;
            }

            Vec3 targetPos = RedstonePigmenManager.targetPos;
            return targetPos != null
                    && RedstonePigmenManager.targetDimension == redstonePigman.level().dimension()
                    && redstonePigman.distanceToSqr(targetPos) > 4.0
                    && !redstonePigman.getNavigation().isStuck();
        }

        @Override
        public boolean canContinueToUse() {
            if (redstonePigman.getTarget() != null) {
                return false;
            }

            Vec3 targetPos = RedstonePigmenManager.targetPos;
            return targetPos != null
                    && redstonePigman.distanceToSqr(targetPos) > 4.0
                    && !redstonePigman.getNavigation().isDone();
        }

        @Override
        public void start() {
            Vec3 targetPos = RedstonePigmenManager.targetPos;
            if (targetPos != null) {
                redstonePigman.getNavigation().moveTo(targetPos.x, targetPos.y, targetPos.z, 2.0d);
                redstonePigman.setAggressive(true);
            }
        }

        @Override
        public void stop() {
            redstonePigman.getNavigation().stop();
            redstonePigman.hasMadeInteraction = true;
            redstonePigman.setAggressive(false);
        }
    }
}