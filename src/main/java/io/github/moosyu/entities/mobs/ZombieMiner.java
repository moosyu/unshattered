package io.github.moosyu.entities.mobs;

import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class ZombieMiner extends Monster {
    private int reloadTicks = 0;

    public ZombieMiner(EntityType<? extends SkeletonMiner> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.@NonNull Builder createAttributes() {
        return UnshatteredUtils.createHostileMobAttributes(100.0f, 13.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.23f);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new FloatGoal(this));
        goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0f));
        goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    protected void playStepSound(@NonNull BlockPos pos, @NonNull BlockState state) {
        playSound(SoundEvents.SKELETON_STEP, 0.15f, 1.0f);
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide()) {
            return;
        }

        if (reloadTicks > 0) {
            reloadTicks--;
        } else if (getMainHandItem().isEmpty()) {
            setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.TNT));
        }
    }

    @Override
    protected @NonNull SoundEvent getHurtSound(@NonNull DamageSource source) {
        return SoundEvents.SKELETON_HURT;
    }

    private static class ThrownMinerTnt extends PrimedTnt {
        private boolean hasHitGround = false;

        public ThrownMinerTnt(Level level, double x, double y, double z, @Nullable LivingEntity owner) {
            super(level, x, y, z, owner);
        }

        @Override
        public void tick() {
            super.tick();

            if (!level().isClientSide() && onGround() && getFuse() > 1 && !hasHitGround) {
                setFuse(20);
                hasHitGround = true;
            }
        }
    }
}