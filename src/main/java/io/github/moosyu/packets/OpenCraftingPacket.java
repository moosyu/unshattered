package io.github.moosyu.packets;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

import static io.github.moosyu.Unshattered.MODID;

public record OpenCraftingPacket() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<OpenCraftingPacket> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(MODID, "open_crafting_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, OpenCraftingPacket> STREAM_CODEC = StreamCodec.unit(new OpenCraftingPacket());

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
