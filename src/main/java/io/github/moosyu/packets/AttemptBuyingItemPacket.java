package io.github.moosyu.packets;

import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

public record AttemptBuyingItemPacket(ItemStack itemStack, int price) implements CustomPacketPayload {
    public static final Type<AttemptBuyingItemPacket> TYPE = new Type<>(UnshatteredUtils.getUnshatteredIdentifier("buy_item_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, AttemptBuyingItemPacket> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC, AttemptBuyingItemPacket::itemStack,
            ByteBufCodecs.INT, AttemptBuyingItemPacket::price,
            AttemptBuyingItemPacket::new
    );

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
