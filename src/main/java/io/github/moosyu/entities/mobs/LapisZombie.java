package io.github.moosyu.entities.mobs;

import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.items.UnshatteredItems;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

public class LapisZombie extends Monster implements RangedAttackMob {
    private static final EntityDataAccessor<Integer> DATA_PHASE = SynchedEntityData.defineId(LapisZombie.class, EntityDataSerializers.INT);
    private int phaseTicks;

    public LapisZombie(EntityType<? extends LapisZombie> type, Level level) {
        super(type, level);

        setItemSlot(EquipmentSlot.HEAD, new ItemStack(UnshatteredItems.LAPIS_HELMET.get()));
        setItemSlot(EquipmentSlot.CHEST, new ItemStack(UnshatteredItems.LAPIS_CHESTPLATE.get()));
        setItemSlot(EquipmentSlot.LEGS, new ItemStack(UnshatteredItems.LAPIS_LEGGINGS.get()));
        setItemSlot(EquipmentSlot.FEET, new ItemStack(UnshatteredItems.LAPIS_BOOTS.get()));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Zombie.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.23f)
                .add(UnshatteredAttributeValues.HEALTH.holder, 35.0f)
                .add(UnshatteredAttributeValues.DAMAGE.holder, 2.0f);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new RangedAttackGoal(this, 1.0d, 40, 24.0f));
        goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 8.0f));
        goalSelector.addGoal(3, new RandomLookAroundGoal(this));

        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void performRangedAttack(@NonNull LivingEntity target, float velocity) {
        if (canFire()) {
            setPhase(Phase.BEAM, 30);
            System.out.println("fire beam");
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NonNull Builder builder) {
        super.defineSynchedData(builder);

        builder.define(DATA_PHASE, Phase.IDLE.ordinal());
    }

    public Phase getPhase() {
        return Phase.values()[entityData.get(DATA_PHASE)];
    }

    private void setPhase(Phase phase, int ticks) {
        entityData.set(DATA_PHASE, phase.ordinal());
        phaseTicks = ticks;
    }

    public boolean canFire() {
        return getPhase() == Phase.IDLE;
    }

    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide() && phaseTicks > 0 && --phaseTicks == 0) {
            switch (getPhase()) {
                case BEAM -> setPhase(Phase.COOLDOWN, 20);
                case COOLDOWN -> setPhase(Phase.IDLE, 0);
                default -> {}
            }
        }
    }

    public enum Phase {
        IDLE,
        BEAM,
        COOLDOWN
    }

    public static class LapisZombieRenderState extends HumanoidRenderState {
        public Phase phase = Phase.IDLE;
    }

    @Override
    protected @NonNull SoundEvent getHurtSound(@NonNull DamageSource source) {
        return SoundEvents.ZOMBIE_HURT;
    }
}
