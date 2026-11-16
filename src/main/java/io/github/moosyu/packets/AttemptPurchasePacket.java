package io.github.moosyu.packets;

import io.github.moosyu.data.ItemCount;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Optional;

/**
 * @param sold the item count attempting to be purchased
 * @param price the base price for a single one of the sold item
 * @param tradeRequirements base item requirements to buy a single one of the sold item
 */
public record AttemptPurchasePacket(ItemCount sold, Optional<Integer> price, Optional<List<ItemCount>> tradeRequirements) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<AttemptPurchasePacket> TYPE = new CustomPacketPayload.Type<>(UnshatteredUtils.getUnshatteredIdentifier("attempt_purchase_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, AttemptPurchasePacket> STREAM_CODEC = StreamCodec.composite(
            ItemCount.STREAM_CODEC, AttemptPurchasePacket::sold,
            ByteBufCodecs.optional(ByteBufCodecs.VAR_INT), AttemptPurchasePacket::price,
            ByteBufCodecs.optional(ItemCount.STREAM_CODEC.apply(ByteBufCodecs.list())), AttemptPurchasePacket::tradeRequirements,
            AttemptPurchasePacket::new
    );

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
