package io.github.moosyu.data.regions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.moosyu.events.DataPackRegistryHandler;
import io.github.moosyu.util.UnshatteredCodecs;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFixedCodec;
import org.joml.Vector2i;

/**
 * a boundary in the world for a given region
 * are to not take into account the z position
 * @param region the region having a boundary made for it
 * @param boundaryCoordinates the shape of the hypothetical rectangle around each region. these can overlap with each other.
 */
public record RegionBoundary(Holder<Region> region, BoundaryCoordinates boundaryCoordinates) {
    public static final Codec<RegionBoundary> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    RegistryFixedCodec.create(DataPackRegistryHandler.REGION_REGISTRY_KEY).fieldOf("region").forGetter(RegionBoundary::region),
                    BoundaryCoordinates.CODEC.fieldOf("boundary_coordinates").forGetter(RegionBoundary::boundaryCoordinates)
            ).apply(instance, RegionBoundary::new)
    );
}
