package io.github.moosyu.data.drops;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.moosyu.items.ItemRange;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jspecify.annotations.Nullable;

public record ItemReward(ItemRange itemRange) implements DropReward {
    public ItemReward(ItemLike item) {
        this(new ItemRange(item));
    }

    public static final MapCodec<ItemReward> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(ItemRange.CODEC.fieldOf("item_range").forGetter(ItemReward::itemRange)).apply(instance, ItemReward::new)
    );

    @Override public Type type() {
        return Type.ITEM;
    }

    @Override
    public float grant(ServerPlayer player, RandomSource random, @Nullable Double fortuneAmount, float modifiedPercentage) {
        int itemCount = UnshatteredUtils.getItemsCount(fortuneAmount != null ? fortuneAmount : 0, itemRange.getDropAmount(random));

        UnshatteredUtils.addItemStackToInventory(player, new ItemStack(itemRange.item(), itemCount));
        return itemCount;
    }
}
