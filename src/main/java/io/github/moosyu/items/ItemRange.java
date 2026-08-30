package io.github.moosyu.items;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

/**
 * stores an item and a number range
 * @param item
 * @param minAmount
 * @param maxAmount
 */
public record ItemRange(Item item, int minAmount, int maxAmount) {
    public ItemRange(Item item) {
        this(item, 1, 1);
    }

    public ItemRange(Item item, int amount) {
        this(item, amount, amount);
    }

    public static final Codec<ItemRange> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(ItemRange::item),
                    Codec.INT.fieldOf("min_amount").forGetter(ItemRange::minAmount),
                    Codec.INT.fieldOf("max_amount").forGetter(ItemRange::maxAmount)
            ).apply(instance, ItemRange::new)
    );
}
