package io.github.moosyu.entities.mobs;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.JumpControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.rabbit.Rabbit;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

import javax.annotation.Nullable;

public class OasisRabbit extends PathfinderMob {
    public static final double STROLL_SPEED_MOD = 0.6;
    public static final double FLEE_SPEED_MOD = 2.2;
    private static final int JUMP_DELAY_TICKS = 10;
    private static final int PANIC_JUMP_DELAY_TICKS = 3;
    private static final int JUMP_DURATION_IN_TICKS = 15;
    private static final int IDLE_MINIMAL_DURATION_TICKS = 180;
    private static final EntityDataAccessor<Integer> DATA_TYPE_ID = SynchedEntityData.defineId(OasisRabbit.class, EntityDataSerializers.INT);

    public final AnimationState hopAnimationState = new AnimationState();
    public final AnimationState idleHeadTiltAnimationState = new AnimationState();
    private int idleAnimationTimeout = random.nextInt(40) + IDLE_MINIMAL_DURATION_TICKS;

    private int jumpTicks;
    private int jumpDuration;
    private boolean wasOnGround;
    private int jumpDelayTicks;

    public OasisRabbit(EntityType<? extends OasisRabbit> type, Level level) {
        super(type, level);
        jumpControl = new RabbitJumpControl(this);
        moveControl = new RabbitMoveControl(this);
        setSpeedModifier(0.0);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new FloatGoal(this));
        goalSelector.addGoal(1, new ClimbOnTopOfPowderSnowGoal(this, level()));
        goalSelector.addGoal(1, new RabbitPanicGoal(this, FLEE_SPEED_MOD));
        goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, STROLL_SPEED_MOD));
        goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 10.0f));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 3.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NonNull Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(DATA_TYPE_ID, Rabbit.Variant.DEFAULT.id());
    }

    @Override
    protected float getJumpPower() {
        float baseJumpPower = 0.3f;
        if (moveControl.getSpeedModifier() <= 0.6) {
            baseJumpPower = 0.2f;
        }

        Path path = navigation.getPath();
        if (path != null && !path.isDone()) {
            Vec3 nextPos = path.getNextEntityPos(this);
            if (nextPos.y > getY() + 0.5) {
                baseJumpPower = 0.5f;
            }
        }

        if (horizontalCollision || jumping && moveControl.getWantedY() > getY() + 0.5) {
            baseJumpPower = 0.5f;
        }

        return super.getJumpPower(baseJumpPower / 0.42f);
    }

    @Override
    public void jumpFromGround() {
        super.jumpFromGround();
        if (moveControl.getSpeedModifier() > 0.0) {
            double horizontalSpeedSqr = getDeltaMovement().horizontalDistanceSqr();
            if (horizontalSpeedSqr < 0.01) {
                moveRelative(0.1f, new Vec3(0.0, 1.5, 1.0));
            }
        }

        if (!level().isClientSide()) {
            level().broadcastEntityEvent(this, (byte)1);
        }
    }

    public float getJumpCompletion(float a) {
        return this.jumpDuration == 0 ? 0.0F : (this.jumpTicks + a) / this.jumpDuration;
    }

    public void setSpeedModifier(double speed) {
        getNavigation().setSpeedModifier(speed);
        moveControl.setWantedPosition(moveControl.getWantedX(), moveControl.getWantedY(), moveControl.getWantedZ(), speed);
    }

    @Override
    public void setJumping(boolean jump) {
        super.setJumping(jump);
        if (jump) {
            playSound(getJumpSound(), getSoundVolume(), ((random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f) * 0.8f);
        }
    }

    public void startJumping() {
        setJumping(true);
        jumpDuration = JUMP_DURATION_IN_TICKS;
        jumpTicks = 0;
    }

    @Override
    public void customServerAiStep(@NonNull ServerLevel level) {
        if (this.jumpDelayTicks > 0) {
            this.jumpDelayTicks--;
        }

        if (this.onGround()) {
            if (!this.wasOnGround) {
                this.setJumping(false);
                this.checkLandingDelay();
            }

            RabbitJumpControl jumpControl = (RabbitJumpControl)this.jumpControl;
            if (!jumpControl.wantJump()) {
                if (this.moveControl.hasWanted() && this.jumpDelayTicks == 0) {
                    Path path = this.navigation.getPath();
                    Vec3 pos = new Vec3(this.moveControl.getWantedX(), this.moveControl.getWantedY(), this.moveControl.getWantedZ());
                    if (path != null && !path.isDone()) {
                        pos = path.getNextEntityPos(this);
                    }

                    this.facePoint(pos.x, pos.z);
                    this.startJumping();
                }
            } else if (!jumpControl.canJump()) {
                this.enableJumpControl();
            }
        }

        this.wasOnGround = this.onGround();
    }


    @Override
    public boolean canSpawnSprintParticle() {
        return false;
    }

    private void facePoint(double x, double z) {
        setYRot((float)(Mth.atan2(z - getZ(), x - getX()) * 180.0f / (float)Math.PI) - 90.0f);
    }

    private void enableJumpControl() {
        ((RabbitJumpControl)jumpControl).setCanJump(true);
    }

    private void disableJumpControl() {
        ((RabbitJumpControl)jumpControl).setCanJump(false);
    }

    private void setLandingDelay() {
        jumpDelayTicks = moveControl.getSpeedModifier() < FLEE_SPEED_MOD ? JUMP_DELAY_TICKS : PANIC_JUMP_DELAY_TICKS;
    }

    private void checkLandingDelay() {
        setLandingDelay();
        disableJumpControl();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (jumpTicks != jumpDuration) {
            jumpTicks++;
        } else if (jumpDuration != 0) {
            jumpTicks = 0;
            jumpDuration = 0;
            setJumping(false);
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 1) {
            spawnSprintParticle();
            jumpDuration = JUMP_DURATION_IN_TICKS;
            jumpTicks = 0;
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    public @NonNull Vec3 getLeashOffset() {
        return new Vec3(0.0, 0.6f * getEyeHeight(), getBbWidth() * 0.4f);
    }

    @Override
    public void setLeashData(Leashable.LeashData leashData) {
        super.setLeashData(leashData);
        idleHeadTiltAnimationState.stop();
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (level().isClientSide()) {
            setupAnimationStates();
        }
    }

    private void setupAnimationStates() {
        if (shouldPlayIdleAnimation()) {
            idleAnimationTimeout = random.nextInt(40) + IDLE_MINIMAL_DURATION_TICKS;
            idleHeadTiltAnimationState.start(tickCount);
        } else if (jumpTicks > 0) {
            hopAnimationState.startIfStopped(tickCount);
            idleHeadTiltAnimationState.stop();
        } else {
            idleAnimationTimeout--;
            hopAnimationState.stop();
        }
    }

    private boolean shouldPlayIdleAnimation() {
        return idleAnimationTimeout <= 0
                && (getLeashData() == null || getLeashData().leashHolder == null)
                && !isNoAi();
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        return level.getBlockState(pos.below()).is(Blocks.GRASS_BLOCK) ? 10.0f : level.getPathfindingCostFromLightLevels(pos);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 120;
    }

    protected SoundEvent getJumpSound() {
        return SoundEvents.RABBIT_JUMP;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.RABBIT_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(@NonNull DamageSource source) {
        return SoundEvents.RABBIT_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.RABBIT_DEATH;
    }

    @Override
    public @NonNull SoundSource getSoundSource() {
        return SoundSource.NEUTRAL;
    }

    @Override
    protected void addAdditionalSaveData(@NonNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.store("Variant", Rabbit.Variant.CODEC, getVariant());
    }

    @Override
    protected void readAdditionalSaveData(@NonNull ValueInput input) {
        super.readAdditionalSaveData(input);
        setVariant(input.read("Variant", Rabbit.Variant.CODEC).orElse(Rabbit.Variant.DEFAULT));
    }

    public Rabbit.Variant getVariant() {
        return Rabbit.Variant.byId(entityData.get(DATA_TYPE_ID));
    }

    private void setVariant(Rabbit.Variant variant) {
        entityData.set(DATA_TYPE_ID, variant.id());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> @Nullable T get(@NonNull DataComponentType<? extends T> type) {
        return type == DataComponents.RABBIT_VARIANT ? castComponentValue((DataComponentType<T>)type, getVariant()) : super.get(type);
    }

    @Override
    protected void applyImplicitComponents(@NonNull DataComponentGetter components) {
        applyImplicitComponentIfPresent(components, DataComponents.RABBIT_VARIANT);
        super.applyImplicitComponents(components);
    }

    @Override
    protected <T> boolean applyImplicitComponent(@NonNull DataComponentType<T> type, @NonNull T value) {
        if (type == DataComponents.RABBIT_VARIANT) {
            setVariant(castComponentValue(DataComponents.RABBIT_VARIANT, value));
            return true;
        }
        return super.applyImplicitComponent(type, value);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @Nullable SpawnGroupData finalizeSpawn(@NonNull ServerLevelAccessor level, @NonNull DifficultyInstance difficulty, @NonNull EntitySpawnReason spawnReason, @Nullable SpawnGroupData groupData) {
        Rabbit.Variant variant = getRandomRabbitVariant(level, blockPosition());
        if (groupData instanceof RabbitGroupData(Rabbit.Variant variant1)) {
            variant = variant1;
        } else {
            groupData = new RabbitGroupData(variant);
        }

        setVariant(variant);
        return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
    }

    private static Rabbit.Variant getRandomRabbitVariant(LevelAccessor level, BlockPos pos) {
        Holder<Biome> biome = level.getBiome(pos);
        int roll = level.getRandom().nextInt(100);
        if (biome.is(BiomeTags.SPAWNS_WHITE_RABBITS)) {
            return roll < 80 ? Rabbit.Variant.WHITE : Rabbit.Variant.WHITE_SPLOTCHED;
        } else if (biome.is(BiomeTags.SPAWNS_GOLD_RABBITS)) {
            return Rabbit.Variant.GOLD;
        } else {
            return roll < 50 ? Rabbit.Variant.BROWN : (roll < 90 ? Rabbit.Variant.SALT : Rabbit.Variant.BLACK);
        }
    }

    public record RabbitGroupData(Rabbit.Variant variant) implements SpawnGroupData {}

    public static class RabbitJumpControl extends JumpControl {
        private final OasisRabbit rabbit;
        private boolean canJump;

        public RabbitJumpControl(OasisRabbit rabbit) {
            super(rabbit);
            this.rabbit = rabbit;
        }

        public boolean wantJump() {
            return jump;
        }

        public boolean canJump() {
            return canJump;
        }

        public void setCanJump(boolean canJump) {
            this.canJump = canJump;
        }

        @Override
        public void tick() {
            if (jump) {
                rabbit.startJumping();
                jump = false;
            }
        }
    }

    private static class RabbitMoveControl extends MoveControl {
        private final OasisRabbit rabbit;
        private double nextJumpSpeed;

        public RabbitMoveControl(OasisRabbit rabbit) {
            super(rabbit);
            this.rabbit = rabbit;
        }

        @Override
        public void tick() {
            if (rabbit.onGround() && !rabbit.jumping && !((RabbitJumpControl)rabbit.jumpControl).wantJump()) {
                rabbit.setSpeedModifier(0.0);
            } else if (hasWanted() || operation == MoveControl.Operation.JUMPING) {
                rabbit.setSpeedModifier(nextJumpSpeed);
            }

            super.tick();
        }

        @Override
        public void setWantedPosition(double x, double y, double z, double speedModifier) {
            if (rabbit.isInWater()) {
                speedModifier = 1.5;
            }

            super.setWantedPosition(x, y, z, speedModifier);
            if (speedModifier > 0.0) {
                nextJumpSpeed = speedModifier;
            }
        }
    }

    private static class RabbitPanicGoal extends PanicGoal {
        private final OasisRabbit rabbit;

        public RabbitPanicGoal(OasisRabbit rabbit, double speedModifier) {
            super(rabbit, speedModifier);
            this.rabbit = rabbit;
        }

        @Override
        public void tick() {
            super.tick();
            rabbit.setSpeedModifier(speedModifier);
        }
    }
}