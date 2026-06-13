package io.github.moosyu.mobs;

import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

public class GraveyardZombieVillager extends PathfinderMob {
    public final AnimationState idleAnimationState = new AnimationState();

    protected GraveyardZombieVillager(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
    }
}
