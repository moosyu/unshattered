package io.github.moosyu.entities.mobs;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class SlimeChild extends UnshatteredSlime {
    public SlimeChild(EntityType<? extends Mob> type, Level level) {
        super(type, level);
    }
}
