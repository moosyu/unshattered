package io.github.moosyu.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.joml.Vector2i;

/**
 * random codecs for non-specific data types
 */
public final class UnshatteredCodecs {
    public static final Codec<Vector2i> VECTOR_2I_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("x").forGetter(v -> v.x),
                    Codec.INT.fieldOf("z").forGetter(v -> v.y)
            ).apply(instance, Vector2i::new)
    );

    public static StreamCodec<ByteBuf, Vector2i> VECTOR_2I_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, v -> v.x,
            ByteBufCodecs.INT, v -> v.y,
            Vector2i::new
    );
}
