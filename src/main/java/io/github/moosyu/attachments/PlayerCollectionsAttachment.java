package io.github.moosyu.attachments;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public record PlayerCollectionsAttachment(Map<Holder<Item>, Integer> collectedItems) {
    public PlayerCollectionsAttachment() {
        this(new HashMap<>());
    }

    public PlayerCollectionsAttachment(Map<Holder<Item>, Integer> collectedItems) {
        this.collectedItems = new HashMap<>(collectedItems);
    }

    public void addPickedUpItem(ItemStack itemStack) {
        this.collectedItems.put(itemStack.typeHolder(), this.collectedItems.getOrDefault(itemStack.typeHolder(), 0) + itemStack.count());
    }

    public int getCount(Holder<Item> item) {
        return this.collectedItems.getOrDefault(item, 0);
    }

    public Map<Holder<Item>, Integer> getMap() {
        return this.collectedItems;
    }

    public static final Codec<PlayerCollectionsAttachment> RECORD_CODEC = Codec.unboundedMap(BuiltInRegistries.ITEM.holderByNameCodec(), Codec.INT)
            .xmap(PlayerCollectionsAttachment::new, PlayerCollectionsAttachment::collectedItems);

    private static final StreamCodec<RegistryFriendlyByteBuf, Map<Holder<Item>, Integer>> MAP_STREAM_CODEC =
            ByteBufCodecs.map(
                    HashMap::new,
                    ByteBufCodecs.holderRegistry(Registries.ITEM),
                    ByteBufCodecs.INT
            );

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerCollectionsAttachment> STREAM_CODEC = MAP_STREAM_CODEC.map(PlayerCollectionsAttachment::new, PlayerCollectionsAttachment::getMap);
}
