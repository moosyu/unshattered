package io.github.moosyu.packets;

import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Optional;

/**
 * @param soldItemStack the itemstack attempting to be purchased
 * @param price the base price for a single one of the sold item
 * @param itemTradeRequirements base item requirements to buy a single one of the sold item
 */
public record AttemptPurchasePacket(ItemStack soldItemStack, Optional<Integer> price, Optional<List<ItemStack>> itemTradeRequirements) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<AttemptPurchasePacket> TYPE = new CustomPacketPayload.Type<>(UnshatteredUtils.getUnshatteredIdentifier("attempt_purchase_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, AttemptPurchasePacket> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC, AttemptPurchasePacket::soldItemStack,
            ByteBufCodecs.optional(ByteBufCodecs.INT), AttemptPurchasePacket::price,
            ByteBufCodecs.optional(ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list())), AttemptPurchasePacket::itemTradeRequirements,
            AttemptPurchasePacket::new
    );

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
