package io.github.moosyu.fishing;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;

import java.util.function.Predicate;

public record FishingMobEntry(EntityType<?> entity, Predicate<Player> condition) implements FishingEntry {
    public FishingMobEntry(EntityType<?> entity) {
        this(entity, _ -> true);
    }

    @Override
    public FishingRewardTypes type() {
        return FishingRewardTypes.MOB;
    }
}
