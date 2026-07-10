package io.github.moosyu.fishing;

import net.minecraft.world.entity.player.Player;

import java.util.function.Predicate;

public interface FishingEntry {
    FishingRewardTypes type();
    Predicate<Player> condition();
}
