package io.github.moosyu.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;

public record ShopItem(Holder<Item> item, int price, boolean sellMultiple) {
    public static Codec<ShopItem> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Item.CODEC.fieldOf("item").forGetter(ShopItem::item),
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("price").forGetter(ShopItem::price),
                    Codec.BOOL.fieldOf("sell_multiple").forGetter(ShopItem::sellMultiple)
            ).apply(instance, ShopItem::new)
    );

    public static StreamCodec<RegistryFriendlyByteBuf, ShopItem> STREAM_CODEC = StreamCodec.composite(
            Item.STREAM_CODEC, ShopItem::item,
            ByteBufCodecs.INT, ShopItem::price,
            ByteBufCodecs.BOOL, ShopItem::sellMultiple,
            ShopItem::new
    );
}