package io.github.moosyu.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public record VendorItem(Item item, boolean sellMultiple, Optional<Integer> price, Optional<List<ItemCount>> itemTradeRequirements) {
    public VendorItem(ItemLike item, int price) {
        this(item.asItem(), true, Optional.of(price), Optional.empty());
    }


    public VendorItem(ItemLike item, boolean sellMultiple, int price) {
        this(item.asItem(), sellMultiple, Optional.of(price), Optional.empty());
    }

    // sized ingredients are used because itemstacks store components etc so datagen doesnt work with them
    // idk if its the best option but i cant really be bothered looking and it looks fine
    public VendorItem(ItemLike item, boolean sellMultiple, ItemCount... itemTradeRequirements) {
        this(item.asItem(), sellMultiple, Optional.empty(), Optional.of(Arrays.stream(itemTradeRequirements).toList()));
    }

    public static Codec<VendorItem> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(VendorItem::item),
                    Codec.BOOL.fieldOf("sell_multiple").forGetter(VendorItem::sellMultiple),
                    ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("price").forGetter(VendorItem::price),
                    ItemCount.CODEC.listOf().optionalFieldOf("item_requirements").forGetter(VendorItem::itemTradeRequirements)
            ).apply(instance, VendorItem::new)
    );

    public static StreamCodec<RegistryFriendlyByteBuf, VendorItem> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.registry(Registries.ITEM), VendorItem::item,
            ByteBufCodecs.BOOL, VendorItem::sellMultiple,
            ByteBufCodecs.optional(ByteBufCodecs.VAR_INT), VendorItem::price,
            ByteBufCodecs.optional(ItemCount.STREAM_CODEC.apply(ByteBufCodecs.list())), VendorItem::itemTradeRequirements,
            VendorItem::new
    );
}