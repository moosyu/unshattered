package io.github.moosyu.packets;

import io.github.moosyu.data.recipes.ForgeRecipe;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jspecify.annotations.NonNull;

public record AttemptForgeItemPacket(ForgeRecipe forgeRecipe, int forgeSlotIndex) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<AttemptForgeItemPacket> TYPE = new CustomPacketPayload.Type<>(UnshatteredUtils.getUnshatteredIdentifier("attempt_forge_item_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, AttemptForgeItemPacket> STREAM_CODEC = StreamCodec.composite(
            ForgeRecipe.STREAM_CODEC, AttemptForgeItemPacket::forgeRecipe,
            ByteBufCodecs.INT, AttemptForgeItemPacket::forgeSlotIndex,
            AttemptForgeItemPacket::new
    );

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
