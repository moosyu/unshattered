package io.github.moosyu.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.ItemLike;

public record ItemCount(ItemLike item, int count) {
    public static final Codec<ItemCount> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("ingredient").forGetter(itemCount -> itemCount.item.asItem()),
                    Codec.INT.fieldOf("count").forGetter(ItemCount::count))
            .apply(instance, ItemCount::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, ItemCount> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.registry(Registries.ITEM), itemCount -> itemCount.item().asItem(),
            ByteBufCodecs.VAR_INT, ItemCount::count,
            ItemCount::new
    );
}
