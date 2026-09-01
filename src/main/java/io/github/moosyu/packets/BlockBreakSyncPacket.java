package io.github.moosyu.packets;

import io.github.moosyu.data.regen.RegenPaths;
import io.github.moosyu.events.DataPackRegistryHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import org.jspecify.annotations.NonNull;

import static io.github.moosyu.Unshattered.MODID;

public record BlockBreakSyncPacket(BlockPos pos, ResourceKey<RegenPaths.RegenPath> regenPathId, int regenPathIndex) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<BlockBreakSyncPacket> TYPE = new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(MODID, "block_break_sync_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, BlockBreakSyncPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, BlockBreakSyncPacket::pos,
            ResourceKey.streamCodec(DataPackRegistryHandler.REGEN_PATH_REGISTRY_KEY), BlockBreakSyncPacket::regenPathId,
            ByteBufCodecs.VAR_INT, BlockBreakSyncPacket::regenPathIndex,
            BlockBreakSyncPacket::new
    );

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
