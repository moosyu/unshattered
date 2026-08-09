package io.github.moosyu.data.regions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.moosyu.Unshattered;
import io.github.moosyu.util.UnshatteredCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import org.joml.Vector2i;

import javax.annotation.Nullable;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.events.DataPackRegistryHandler.REGION_BOUNDARY_REGISTRY_KEY;

/**
 * coordinates to create a rectangle. z axis isn't tracked as it'd be pointless (i'll probably be instancing underground areas, but we'll see)
 * priority 0 -> base (entire world) region so have a maximum of 1 at that level. anything above that is for normal regions.
 * when setting these, if the player is facing east then width
 * @param topLeftCornerCoordinates the bottom left point of the rectangle. these integers are meant to relate to points in the minecraft world so just pretend Vector2i's y is minecraft's z
 * @param bottomRightCornerCoordinates
 * @param priority whether this takes priority over other regions the player is in. highest priority wins. two regions with the same priority wont throw an error but which region is picked becomes unpredictable.
 */
public record BoundaryCoordinates(Vector2i topLeftCornerCoordinates, Vector2i bottomRightCornerCoordinates, int priority) {
    public static final Codec<BoundaryCoordinates> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    UnshatteredCodecs.VECTOR_2I_CODEC.fieldOf("top_left_corner_coordinates").forGetter(BoundaryCoordinates::topLeftCornerCoordinates),
                    UnshatteredCodecs.VECTOR_2I_CODEC.fieldOf("bottom_right_corner_coordinates").forGetter(BoundaryCoordinates::bottomRightCornerCoordinates),
                    Codec.INT.fieldOf("priority").forGetter(BoundaryCoordinates::priority)
            ).apply(instance, BoundaryCoordinates::new)
    );

    /**
     * get coordinates for specific region. depends on the boundary following the naming convention of region_bounds.
     * @param serverLevel the server level (probably from the overworld)
     * @param region the region that the coords are needed for
     * @return the boundary coordinates of the specified region (as long as the region is real)
     */
    @Nullable
    public static BoundaryCoordinates getRegionCoordinates(ServerLevel serverLevel, ResourceKey<Region> region) {
        if (serverLevel != null) {
            RegionBoundary boundary = serverLevel.registryAccess()
                    .lookupOrThrow(REGION_BOUNDARY_REGISTRY_KEY)
                    .getValue(Identifier.fromNamespaceAndPath(MODID, region.identifier().getPath() + "_bounds"));
            if (boundary != null) {
                return boundary.boundaryCoordinates();
            }
        }
        Unshattered.LOGGER.error("get region coordinates failed with {}", region.identifier());
        return null;
    }

    /**
     * just to simplify the process when i need widths and heights, shits long
     * @return x as the rectangle width and y as the height
     */
    public Vector2i getRectangleLengths() {
        return new Vector2i(Math.abs(topLeftCornerCoordinates.x - bottomRightCornerCoordinates.x), Math.abs(topLeftCornerCoordinates.y - bottomRightCornerCoordinates.y));
    }
}
