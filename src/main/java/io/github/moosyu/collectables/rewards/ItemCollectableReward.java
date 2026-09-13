package io.github.moosyu.collectables.rewards;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.moosyu.data.attachments.PlayerSkillsAttachment;
import net.minecraft.core.registries.BuiltInRegistries;
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

    @Override
    public MapCodec<? extends ItemCollectableReward> codec() {
        return CODEC;
    }

    public static final MapCodec<ItemCollectableReward> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(ItemCollectableReward::item),
                    Codec.INT.fieldOf("amount").forGetter(ItemCollectableReward::amount)
            ).apply(instance, ItemCollectableReward::new)
    );
}
