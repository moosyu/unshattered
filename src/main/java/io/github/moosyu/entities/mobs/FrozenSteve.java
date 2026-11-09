package io.github.moosyu.entities.mobs;

import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

public class FrozenSteve extends Monster {
    public FrozenSteve(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return UnshatteredUtils.createHostileMobAttributes(40.0f, 2.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.28f);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0, false));
        goalSelector.addGoal(2, new RandomStrollGoal(this, 0.8));
        goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0f));
        goalSelector.addGoal(4, new RandomLookAroundGoal(this));

        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    protected @NonNull SoundEvent getHurtSound(@NonNull DamageSource source) {
        return SoundEvents.GLASS_BREAK;
    }

    @Override
    public boolean doHurtTarget(@NonNull ServerLevel level, @NonNull Entity target) {
        if (super.doHurtTarget(level, target)) {
            if (target instanceof ServerPlayer serverPlayer) {
                float currentTemperature = serverPlayer.getData(UnshatteredAttachments.PLAYER_TEMPERATURE.get());
                serverPlayer.setData(UnshatteredAttachments.PLAYER_TEMPERATURE.get(), currentTemperature - 0.5f);
            }

            return true;
        }

        return false;
    }
}
