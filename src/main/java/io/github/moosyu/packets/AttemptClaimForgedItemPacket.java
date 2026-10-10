package io.github.moosyu.packets;

import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

public record AttemptClaimForgedItemPacket(ItemStack claimedItem, int forgeSlotIndex, long endTime) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<AttemptClaimForgedItemPacket> TYPE = new CustomPacketPayload.Type<>(UnshatteredUtils.getUnshatteredIdentifier("attempt_claim_forged_item_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, AttemptClaimForgedItemPacket> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC, AttemptClaimForgedItemPacket::claimedItem,
            ByteBufCodecs.INT, AttemptClaimForgedItemPacket::forgeSlotIndex,
            ByteBufCodecs.LONG, AttemptClaimForgedItemPacket::endTime,
            AttemptClaimForgedItemPacket::new
    );

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
