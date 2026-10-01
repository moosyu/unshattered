package io.github.moosyu.entities;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
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
