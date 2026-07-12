package io.github.moosyu.collectables.rewards;

import net.minecraft.world.entity.player.Player;


public interface CollectableReward {
    RewardCategories category();
    void reward(Player player);
}
