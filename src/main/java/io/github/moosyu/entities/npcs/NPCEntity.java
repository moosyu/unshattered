package io.github.moosyu.entities.npcs;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import org.jspecify.annotations.NonNull;

public class NPCEntity extends Mob {
    private final int nameColour;

    public NPCEntity(EntityType<? extends Mob> type, Level level, int nameColour) {
        super(type, level);

        this.nameColour = nameColour;
    }

    public static AttributeSupplier.Builder createNpcAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0d)
                .add(Attributes.MOVEMENT_SPEED, 0.0d)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0d)
                .add(Attributes.FOLLOW_RANGE, 1.0d)
                .add(Attributes.WAYPOINT_TRANSMIT_RANGE, 1.0d)
                .add(Attributes.WAYPOINT_RECEIVE_RANGE, 1.0d)
                .add(Attributes.STEP_HEIGHT, 0.0d)
                .add(Attributes.MOVEMENT_EFFICIENCY, 0.0d)
                .add(Attributes.MOVEMENT_SPEED, 0.7d)
                .add(Attributes.SCALE, 1.0d)
                .add(Attributes.GRAVITY, 0.08d)
                .add(Attributes.ARMOR, 0.0d)
                .add(Attributes.ARMOR_TOUGHNESS, 0.0d)
                .add(Attributes.MAX_ABSORPTION, 0.0d)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.0d)
                .add(Attributes.SAFE_FALL_DISTANCE, 1024.0d)
                .add(Attributes.FALL_DAMAGE_MULTIPLIER, 0.0d);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new LookAtPlayerGoal(this, Player.class, 10.0f, 1.0f));
    }

    @Override public boolean isInvulnerableTo(@NonNull ServerLevel level, @NonNull DamageSource source) {
        return true;
    }

    @Override public void kill(@NonNull ServerLevel level) {}

    @Override public boolean removeWhenFarAway(double distSq) {
        return false;
    }

    @Override public void checkDespawn() {}

    @Override public boolean isPushable() {
        return false;
    }

    @Override public boolean shouldTravelInFluid(@NonNull FluidState fluidState) {
        return false;
    }

    @Override public void knockback(double strength, double x, double z) {}

    public int getNametagColour() {
        return nameColour;
    }
}
