package io.github.moosyu.data.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;

public record ItemFuel(int maxFuel, int currentFuel) {
    public static Codec<ItemFuel> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("max_fuel").forGetter(ItemFuel::maxFuel),
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("current_fuel").forGetter(ItemFuel::currentFuel)
                    ).apply(instance, ItemFuel::new)
    );

    public static final StreamCodec<ByteBuf, ItemFuel> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ItemFuel::maxFuel,
            ByteBufCodecs.VAR_INT, ItemFuel::currentFuel,
            ItemFuel::new
    );
}
