package io.github.moosyu.collectables.rewards;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.player.Player;

public interface CollectableReward {
    RewardCategories category();
    void reward(Player player);
    MapCodec<? extends CollectableReward> codec();

    Codec<CollectableReward> CODEC = RewardCategories.CODEC.dispatch("type", CollectableReward::category, RewardCategories::codec);
}
