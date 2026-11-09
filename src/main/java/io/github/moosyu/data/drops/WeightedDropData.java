package io.github.moosyu.data.drops;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record WeightedDropData(DropReward reward, int weight) {
    public static final Codec<WeightedDropData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    DropReward.CODEC.fieldOf("drop_data").forGetter(WeightedDropData::reward),
                    Codec.INT.fieldOf("weight").forGetter(WeightedDropData::weight)
            ).apply(instance, WeightedDropData::new)
    );
}
