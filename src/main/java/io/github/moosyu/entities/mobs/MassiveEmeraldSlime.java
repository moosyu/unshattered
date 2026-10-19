package io.github.moosyu.entities.mobs;

import io.github.moosyu.entities.UnshatteredEntities;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

import java.util.EnumSet;

public class MassiveEmeraldSlime extends UnshatteredSlime {
    private final static float DAMAGE = 12.0f;
    private static final EntityDataAccessor<Integer> DATA_SUMMON_TICKS = SynchedEntityData.defineId(MassiveEmeraldSlime.class, EntityDataSerializers.INT);
    private boolean summonedChildren = false;

    public MassiveEmeraldSlime(EntityType<? extends Mob> type, Level level) {
        super(type, level, DAMAGE);
    }

    public static AttributeSupplier.@NonNull Builder createAttributes() {
        return UnshatteredUtils.createHostileMobAttributes(100.0f, DAMAGE)
                .add(Attributes.ATTACK_KNOCKBACK, 6.0f);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NonNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_SUMMON_TICKS, 0);
    }

    @Override
    protected void addAdditionalSaveData(@NonNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("SummonedChildren", summonedChildren);
    }

    @Override
    protected void readAdditionalSaveData(@NonNull ValueInput input) {
        super.readAdditionalSaveData(input);
        summonedChildren = input.getBooleanOr("SummonedChildren", false);
    }

    private int getSummonTicks() {
        return entityData.get(DATA_SUMMON_TICKS);
    }

    private void setSummonTicks(int ticks) {
        entityData.set(DATA_SUMMON_TICKS, ticks);
    }

    public float getRenderScaleMultiplier(float partialTick) {
        int ticks = getSummonTicks();
        if (ticks <= 0) {
            return 1.0f;
        }

        float elapsedTicks = ticks + partialTick;
        if (elapsedTicks < SlimeSummonChildrenGoal.GROW_TICKS) {
            float growProgress = elapsedTicks / SlimeSummonChildrenGoal.GROW_TICKS;
            return 1.0f + SlimeSummonChildrenGoal.MAX_GROWTH * growProgress * growProgress;
        }
        float shrinkProgress = Math.min((elapsedTicks - SlimeSummonChildrenGoal.GROW_TICKS) / SlimeSummonChildrenGoal.SHRINK_TICKS, 1.0f);
        return 1.0f + SlimeSummonChildrenGoal.MAX_GROWTH * (1.0f - shrinkProgress);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new SlimeSummonChildrenGoal(this));
        goalSelector.addGoal(2, new SlimeFloatGoal(this));
        goalSelector.addGoal(3, new SlimeAttackGoal(this));
        goalSelector.addGoal(4, new SlimeRandomDirectionGoal(this));
        goalSelector.addGoal(5, new SlimeKeepOnJumpingGoal(this));
        targetSelector.addGoal(1,
                new NearestAttackableTargetGoal<>(this,
                        Player.class,
                        10,
                        true,
                        false,
                        (target, _) -> Math.abs(target.getY() - this.getY()) <= 4.0
                )
        );
    }

    protected static class SlimeSummonChildrenGoal extends Goal {
        private static final int GROW_TICKS = 20;
        private static final int SHRINK_TICKS = 6;
        private static final float MAX_GROWTH = 0.25f;
        private static final double LAUNCH_DISTANCE = 5.0;
        private static final double LAUNCH_HORIZONTAL_SPEED = 0.65;
        private static final double LAUNCH_VERTICAL_SPEED = 0.55;
        private static final int RETRY_DELAY = 40;

        private static final Vec3[] CARDINAL_DIRECTIONS = {
                new Vec3(1, 0, 0),
                new Vec3(-1, 0, 0),
                new Vec3(0, 0, 1),
                new Vec3(0, 0, -1)
        };

        private final MassiveEmeraldSlime slime;
        private int retryDelay = 0;

        public SlimeSummonChildrenGoal(MassiveEmeraldSlime mob) {
            slime = mob;
            setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (slime.summonedChildren || !(slime.getTarget() instanceof Player) || !slime.onGround()) {
                return false;
            }

            if (retryDelay > 0) {
                retryDelay--;
                return false;
            }

            Level level = slime.level();
            EntityDimensions dims = UnshatteredEntities.SLIME_CHILD.get().getDimensions();

            boolean haveRoomForChildren = true;
            for (Vec3 direction : CARDINAL_DIRECTIONS) {
                if (hasRoom(level, dims, slime.position().add(direction.scale(LAUNCH_DISTANCE)).add(0, 0.01, 0))
                        || hasRoom(level, dims, slime.position().add(direction.scale(LAUNCH_DISTANCE * 0.5)).add(0, 1.5, 0))
                ) {
                    haveRoomForChildren = false;
                }
            }


            if (!haveRoomForChildren) {
                retryDelay = RETRY_DELAY;
                return false;
            }

            return true;
        }

        @Override
        public boolean canContinueToUse() {
            return !slime.summonedChildren && slime.isAlive();
        }

        @Override
        public boolean isInterruptable() {
            return false;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void start() {
            slime.setSummonTicks(1);
            slime.getNavigation().stop();
            slime.playSound(SoundEvents.SLIME_SQUISH, 2.0f, 0.5f);
        }

        @Override
        public void tick() {
            slime.setSpeed(0.0f);
            LivingEntity target = slime.getTarget();
            if (target != null) {
                slime.lookAt(target, 10.0f, 10.0f);
                if (slime.getMoveControl() instanceof SlimeMoveControl moveControl) {
                    moveControl.setDirection(slime.getYRot());
                }
            }

            int ticks = slime.getSummonTicks() + 1;
            slime.setSummonTicks(ticks);

            if (ticks == GROW_TICKS && slime.level() instanceof ServerLevel serverLevel) {
                Vec3 origin = slime.position().add(0, slime.getBbHeight() * 0.4, 0);

                for (Vec3 direction : CARDINAL_DIRECTIONS) {
                    SlimeChild child = UnshatteredEntities.SLIME_CHILD.get().spawn(serverLevel, slime.blockPosition(), EntitySpawnReason.TRIGGERED);
                    if (child == null) {
                        continue;
                    }

                    child.snapTo(origin.x, origin.y, origin.z, (float) (Mth.atan2(direction.z, direction.x) * Mth.RAD_TO_DEG) - 90.0f, 0.0f);
                    child.setDeltaMovement(direction.x * LAUNCH_HORIZONTAL_SPEED, LAUNCH_VERTICAL_SPEED, direction.z * LAUNCH_HORIZONTAL_SPEED);
                }

                serverLevel.playSound(null, slime.blockPosition(), SoundEvents.SLIME_JUMP, SoundSource.HOSTILE, 2.0f, 0.5f);
                serverLevel.sendParticles(ParticleTypes.ITEM_SLIME, origin.x, origin.y, origin.z, 40, 1.5, 1.0, 1.5, 0.1);

            }
            if (ticks >= GROW_TICKS + SHRINK_TICKS) {
                slime.summonedChildren = true;
            }
        }

        @Override
        public void stop() {
            slime.setSummonTicks(0);
        }

        private static boolean hasRoom(Level level, EntityDimensions dims, Vec3 pos) {
            AABB box = dims.makeBoundingBox(pos);
            return !level.noCollision(box) || level.containsAnyLiquid(box);
        }
    }
}