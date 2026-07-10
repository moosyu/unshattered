package io.github.moosyu.fishing;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

import java.util.function.Predicate;

public record FishingItemEntry(Item item, Predicate<Player> condition) implements FishingEntry {
    public FishingItemEntry(Item item) {
        this(item, _ -> true);
    }
    @Override
    public FishingRewardTypes type() {
        return FishingRewardTypes.MOB;
    }
}
