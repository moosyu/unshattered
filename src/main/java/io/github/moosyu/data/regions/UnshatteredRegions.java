package io.github.moosyu.data.regions;

import io.github.moosyu.events.DataPackRegistryHandler;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import static io.github.moosyu.Unshattered.MODID;

public final class UnshatteredRegions {
    public static final ResourceKey<Region> DEFAULT_REGION = ResourceKey.create(
            DataPackRegistryHandler.REGION_REGISTRY_KEY,
            Identifier.fromNamespaceAndPath(MODID, "unincorporated")
    );

    public static final ResourceKey<Region> PLAINS_REGION = ResourceKey.create(
            DataPackRegistryHandler.REGION_REGISTRY_KEY,
            Identifier.fromNamespaceAndPath(MODID, "plains")
    );
}
