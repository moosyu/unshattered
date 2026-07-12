package io.github.moosyu.collectables.rewards;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record ItemCollectableReward(Item item, int amount) implements CollectableReward {
    @Override
    public RewardCategories category() {
        return RewardCategories.ITEM;
    }

    @Override
    public void reward(Player player) {
        // purposefully not adding the given items to the collections
        player.getInventory().add(new ItemStack(this.item(), this.amount()));
    }
}
