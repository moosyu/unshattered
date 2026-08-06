package io.github.moosyu.data.regions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.moosyu.util.UnshatteredCodecs;
import org.joml.Vector2i;

/**
 * coordinates to create a rectangle. z axis isn't tracked as it'd be pointless (i'll probably be instancing underground areas, but we'll see)
 * @param topLeftCornerCoordinates the bottom left point of the rectangle. these integers are meant to relate to points in the minecraft world so just pretend Vector2i's y is minecraft's z
 * @param width rectangle width
 * @param height rectangle height
 */
public record BoundaryCoordinates(Vector2i topLeftCornerCoordinates, int width, int height) {
    public static final Codec<BoundaryCoordinates> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    UnshatteredCodecs.VECTOR_2I_CODEC.fieldOf("top_left_corner_coordinates").forGetter(BoundaryCoordinates::topLeftCornerCoordinates),
                    Codec.INT.fieldOf("width").forGetter(BoundaryCoordinates::width),
                    Codec.INT.fieldOf("height").forGetter(BoundaryCoordinates::height)
            ).apply(instance, BoundaryCoordinates::new)
    );
}
