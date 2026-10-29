package io.github.moosyu.entities.mobs;

import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.fluids.FluidType;
import org.jspecify.annotations.NonNull;

public class SeaWalker extends Monster {
    private static final VoxelShape LIQUID_COLLISION_SHAPE = Block.column(16.0, 0.0, 14.0);

    public SeaWalker(EntityType<? extends Monster> type, Level level) {
        super(type, level);
        setPathfindingMalus(PathType.WATER, 0.0f);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return UnshatteredUtils.createHostileMobAttributes(30.0f, 2.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.34f);
    }

    @Override
    public boolean canStandOnFluid(FluidState fluid) {
        return fluid.is(FluidTags.WATER);
    }

    @Override
    public boolean isPushedByFluid(@NonNull FluidType type) {
        return false;
    }

    @Override
    protected @NonNull PathNavigation createNavigation(@NonNull Level level) {
        return new SeaWalkerNavigation(this, level);
    }

    @Override
    public @NonNull VoxelShape getLiquidCollisionShape() {
        return LIQUID_COLLISION_SHAPE;
    }

    @Override
    public void tick() {
        super.tick();
        // see Strider#floatStrider
        if (isInWater()) {
            if (CollisionContext.of(this).isAbove(LIQUID_COLLISION_SHAPE, blockPosition(), true)
                    && !level().getFluidState(blockPosition().above()).is(FluidTags.WATER)
            ) {
                setOnGround(true);
            } else {
                setDeltaMovement(getDeltaMovement().scale(0.5).add(0, 0.05, 0));
            }
        }
    }

    private static class SeaWalkerNavigation extends GroundPathNavigation {
        SeaWalkerNavigation(Mob mob, Level level) {
            super(mob, level);
        }

        @Override
        protected @NonNull PathFinder createPathFinder(int maxVisitedNodes) {
            nodeEvaluator = new WalkNodeEvaluator();
            nodeEvaluator.setCanPassDoors(true);
            nodeEvaluator.setCanFloat(true);
            return new PathFinder(nodeEvaluator, maxVisitedNodes);
        }

        @Override
        protected boolean hasValidPathType(@NonNull PathType type) {
            return type == PathType.WATER || super.hasValidPathType(type);
        }

        @Override
        public boolean isStableDestination(@NonNull BlockPos pos) {
            return level.getBlockState(pos).is(Blocks.WATER) || super.isStableDestination(pos);
        }
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(3, new SeaWalkerAttackGoal(this, 1.0, false));
        goalSelector.addGoal(7, new RandomStrollGoal(this, 0.8));
        goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0f));
        goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public void spawnSplashParticles(ServerLevel level, Vec3 origin, Vec3 direction) {
        for (int i = 0; i < 15; i++) {
            level.sendParticles(ParticleTypes.SPLASH,
                    origin.x,
                    origin.y,
                    origin.z,
                    0,
                    direction.x + (random.nextDouble() - 0.5) * 0.3,
                    direction.y + random.nextDouble() * 0.3,
                    direction.z + (random.nextDouble() - 0.5) * 0.3,
                    0.8
            );
        }
    }

    @Override
    public boolean doHurtTarget(@NonNull ServerLevel level, @NonNull Entity target) {
        boolean hit = super.doHurtTarget(level, target);
        if (hit) {
            final Vec3 ORIGIN = getEyePosition().add(getLookAngle().scale(0.5)).subtract(0, 0.2, 0);
            final Vec3 DIRECTION = target.getEyePosition().subtract(ORIGIN).normalize();

            spawnSplashParticles(level, ORIGIN, DIRECTION);
            playSound(SoundEvents.PLAYER_SPLASH, 1.0f, 1.0f);
        }
        return hit;
    }

    @Override
    protected @NonNull SoundEvent getHurtSound(@NonNull DamageSource source) {
        return SoundEvents.DROWNED_HURT_WATER;
    }

    private class SeaWalkerAttackGoal extends MeleeAttackGoal {
        private final SeaWalker seaWalker;
        private int raiseArmTicks;

        public SeaWalkerAttackGoal(SeaWalker seaWalker, double speedModifier, boolean trackTarget) {
            super(seaWalker, speedModifier, trackTarget);
            this.seaWalker = seaWalker;
        }

        @Override
        public void start() {
            super.start();
            raiseArmTicks = 0;
        }

        @Override
        public void stop() {
            super.stop();
            seaWalker.setAggressive(false);
        }

        @Override
        public void tick() {
            super.tick();
            ++raiseArmTicks;
            seaWalker.setAggressive(raiseArmTicks >= 5 && getTicksUntilNextAttack() < getAttackInterval() / 2);
            if (raiseArmTicks % 4 == 0 && level() instanceof ServerLevel serverLevel) {
                spawnSplashParticles(serverLevel, position(), new Vec3(0, 0.1, 0));
            }
        }
    }
}