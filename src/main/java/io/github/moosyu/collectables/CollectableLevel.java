package io.github.moosyu.collectables;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.moosyu.collectables.rewards.CollectableReward;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @param itemRequirement the amount of items required to get <i>to</i> the level from the previous level
 * @param rewards rewards for getting to the level
 */
public record CollectableLevel(int itemRequirement, List<CollectableReward> rewards) {
    public static Codec<CollectableLevel> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("item_requirement").forGetter(CollectableLevel::itemRequirement),
                    CollectableReward.CODEC.listOf().fieldOf("collectable_rewards").forGetter(CollectableLevel::rewards)
            ).apply(instance, CollectableLevel::new)
    );
}
