package io.github.moosyu.collectables;

import io.github.moosyu.collectables.rewards.CollectableReward;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @param itemRequirement the amount of items required to get <i>to</i> the level from the previous level
 * @param rewards rewards for getting to the level
 */
public record CollectableLevel(int itemRequirement, List<CollectableReward> rewards) {}
