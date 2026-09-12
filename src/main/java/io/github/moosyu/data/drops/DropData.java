package io.github.moosyu.data.drops;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.moosyu.items.ItemRange;

public record DropData(ItemRange itemRange, float dropChance) {
    public DropData(ItemRange itemRange) {
        this(itemRange, 1.0f);
    }

    public static Codec<DropData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ItemRange.CODEC.fieldOf("item_range").forGetter(DropData::itemRange),
                    Codec.FLOAT.fieldOf("drop_chance").forGetter(DropData::dropChance)
            ).apply(instance, DropData::new)
    );
}
