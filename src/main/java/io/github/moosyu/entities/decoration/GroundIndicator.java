package io.github.moosyu.entities.decoration;

import io.github.moosyu.entities.UnshatteredEntities;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

public class GroundIndicator extends Entity {
    public static final int DEFAULT_INNER_COLOUR = 0x7FFFA000;
    public static final int DEFAULT_OUTLINE_COLOUR = 0x7FDB8A00;

    // x
    public static final EntityDataAccessor<Float> HALF_WIDTH = SynchedEntityData.defineId(GroundIndicator.class, EntityDataSerializers.FLOAT);
    // z
    public static final EntityDataAccessor<Float> HALF_HEIGHT = SynchedEntityData.defineId(GroundIndicator.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Integer> INNER_COLOUR = SynchedEntityData.defineId(GroundIndicator.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> OUTLINE_COLOUR = SynchedEntityData.defineId(GroundIndicator.class, EntityDataSerializers.INT);
    // in ticks
    public static final EntityDataAccessor<Integer> DURATION = SynchedEntityData.defineId(GroundIndicator.class, EntityDataSerializers.INT);

    public GroundIndicator(EntityType<?> type, Level level) {
        super(type, level);
    }

    public static GroundIndicator spawn(Level level, Vec3 centre, float width, float depth, int durationTicks, int innerColour, int outlineColour) {
        if (level.isClientSide()) {
            return null;
        }

        GroundIndicator indicator = new GroundIndicator(UnshatteredEntities.GROUND_INDICATOR.get(), level);
        indicator.setPos(centre);
        indicator.entityData.set(HALF_WIDTH, width / 2f);
        indicator.entityData.set(HALF_HEIGHT, depth / 2f);
        indicator.entityData.set(INNER_COLOUR, innerColour);
        indicator.entityData.set(OUTLINE_COLOUR, outlineColour);
        indicator.entityData.set(DURATION, Math.max(1, durationTicks));
        level.addFreshEntity(indicator);
        return indicator;
    }

    public static Vec3 snapToGround(Level level, Entity context, Vec3 position) {
        // one block up which should hopefully make sure it doesnt start inside a block
        Vec3 startingPosition = position.add(0, 1, 0);
        BlockHitResult hit = level.clip(new ClipContext(startingPosition, startingPosition.add(0, -16, 0), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, context));
        return hit.getType() == HitResult.Type.MISS ? position : hit.getLocation();
    }

    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide() && tickCount >= getDuration()) {
            discard();
        }
    }


    public float getHalfWidth() {
        return entityData.get(HALF_WIDTH);
    }

    public float getHalfHeight() {
        return entityData.get(HALF_HEIGHT);
    }

    public int getInnerColour() {
        return entityData.get(INNER_COLOUR);
    }

    public int getOutlineColour() {
        return entityData.get(INNER_COLOUR);
    }

    public int getDuration() {
        return entityData.get(DURATION);
    }


    @Override
    protected void defineSynchedData(SynchedEntityData.@NonNull Builder builder) {
        builder.define(HALF_WIDTH, 1.0f);
        builder.define(HALF_HEIGHT, 1.0f);
        builder.define(INNER_COLOUR, DEFAULT_INNER_COLOUR);
        builder.define(OUTLINE_COLOUR, DEFAULT_OUTLINE_COLOUR);
        builder.define(DURATION, 20);
    }

    @Override
    public boolean hurtServer(@NonNull ServerLevel serverLevel, @NonNull DamageSource damageSource, float amount) {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(@NonNull ValueInput valueInput) {}

    @Override
    protected void addAdditionalSaveData(@NonNull ValueOutput valueOutput) {}
}
