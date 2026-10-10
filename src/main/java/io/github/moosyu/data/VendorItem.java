package io.github.moosyu.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.List;
import java.util.Optional;

public record VendorItem(Holder<Item> item, boolean sellMultiple, Optional<Integer> price, Optional<List<ItemStack>> itemTradeRequirements) {
    public VendorItem(ItemLike item, int price) {
        this(BuiltInRegistries.ITEM.wrapAsHolder(item.asItem()), true, Optional.of(price), Optional.empty());
    }


    public VendorItem(ItemLike item, boolean sellMultiple, int price) {
        this(BuiltInRegistries.ITEM.wrapAsHolder(item.asItem()), sellMultiple, Optional.of(price), Optional.empty());
    }

    public VendorItem(ItemLike item, boolean sellMultiple, List<ItemStack> itemTradeRequirements) {
        this(BuiltInRegistries.ITEM.wrapAsHolder(item.asItem()), sellMultiple, Optional.empty(), Optional.of(itemTradeRequirements));
    }

    public static Codec<VendorItem> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Item.CODEC.fieldOf("item").forGetter(VendorItem::item),
                    Codec.BOOL.fieldOf("sell_multiple").forGetter(VendorItem::sellMultiple),
                    ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("price").forGetter(VendorItem::price),
                    ItemStack.CODEC.listOf().optionalFieldOf("item_requirements").forGetter(VendorItem::itemTradeRequirements)
            ).apply(instance, VendorItem::new)
    );

    public static StreamCodec<RegistryFriendlyByteBuf, VendorItem> STREAM_CODEC = StreamCodec.composite(
            Item.STREAM_CODEC, VendorItem::item,
            ByteBufCodecs.BOOL, VendorItem::sellMultiple,
            ByteBufCodecs.optional(ByteBufCodecs.INT), VendorItem::price,
            ByteBufCodecs.optional(ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list())), VendorItem::itemTradeRequirements,
            VendorItem::new
    );
}