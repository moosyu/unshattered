package io.github.moosyu.entities.mob;

import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class Frosty extends PathfinderMob {
    public Frosty(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return UnshatteredUtils.createHostileMobAttributes(400.0f, 3.5f)
                .add(Attributes.MOVEMENT_SPEED, 0.2f);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new FrostyAttackGoal(this));
        goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0));
        goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0f));
        goalSelector.addGoal(4, new RandomLookAroundGoal(this));

        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return SoundEvents.SNOW_GOLEM_AMBIENT;
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(@NonNull DamageSource source) {
        return SoundEvents.SNOW_GOLEM_HURT;
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return SoundEvents.SNOW_GOLEM_DEATH;
    }

    public static class FrostyAttackGoal extends Goal {
        private final Frosty frosty;

        public FrostyAttackGoal(Frosty frosty) {
            this.frosty = frosty;
        }

        @Override
        public boolean canUse() {
            return false;
        }
    }
}
