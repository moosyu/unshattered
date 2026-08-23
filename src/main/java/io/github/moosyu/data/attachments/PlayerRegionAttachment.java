package io.github.moosyu.data.attachments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.moosyu.data.regions.Region;
import io.github.moosyu.events.DataPackRegistryHandler;
import io.github.moosyu.util.UnshatteredCodecs;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import org.joml.Vector2i;

/**
 *
 * @param regionKey the key for the region the player is currently in
 * @param currentBlockPos
 */
public record PlayerRegionAttachment(ResourceKey<Region> regionKey, BlockPos currentBlockPos) {
    public static final Codec<PlayerRegionAttachment> RECORD_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceKey.codec(DataPackRegistryHandler.REGION_REGISTRY_KEY).fieldOf("region").forGetter(PlayerRegionAttachment::regionKey),
                    BlockPos.CODEC.fieldOf("current_block_pos").forGetter(PlayerRegionAttachment::currentBlockPos)
            ).apply(instance, PlayerRegionAttachment::new)
    );

    public static final StreamCodec<ByteBuf, PlayerRegionAttachment> STREAM_CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(DataPackRegistryHandler.REGION_REGISTRY_KEY), PlayerRegionAttachment::regionKey,
            BlockPos.STREAM_CODEC, PlayerRegionAttachment::currentBlockPos,
            PlayerRegionAttachment::new
    );
}