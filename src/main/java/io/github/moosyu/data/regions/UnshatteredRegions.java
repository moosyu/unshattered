package io.github.moosyu.data.regions;

import io.github.moosyu.events.DataPackRegistryHandler;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import static io.github.moosyu.Unshattered.MODID;

public final class UnshatteredRegions {
    // used to define world size
    public static final ResourceKey<Region> DEFAULT_REGION = ResourceKey.create(
            DataPackRegistryHandler.REGION_REGISTRY_KEY,
            UnshatteredUtils.getUnshatteredIdentifier("unincorporated")
    );

    public static final ResourceKey<Region> PLAINS_REGION = ResourceKey.create(
            DataPackRegistryHandler.REGION_REGISTRY_KEY,
            UnshatteredUtils.getUnshatteredIdentifier("plains")
    );

    public static final ResourceKey<Region> FORGOTTEN_DWELLING = ResourceKey.create(
            DataPackRegistryHandler.REGION_REGISTRY_KEY,
            UnshatteredUtils.getUnshatteredIdentifier("forgotten_dwelling")
    );

    public static final ResourceKey<Region> HYTHE = ResourceKey.create(
            DataPackRegistryHandler.REGION_REGISTRY_KEY,
            UnshatteredUtils.getUnshatteredIdentifier("hythe")
    );

    public static final ResourceKey<Region> SKY = ResourceKey.create(
            DataPackRegistryHandler.REGION_REGISTRY_KEY,
            UnshatteredUtils.getUnshatteredIdentifier("sky")
    );

    public static final ResourceKey<Region> OASIS = ResourceKey.create(
            DataPackRegistryHandler.REGION_REGISTRY_KEY,
            UnshatteredUtils.getUnshatteredIdentifier("oasis")
    );

    public static final ResourceKey<Region> JERRY_WORKSHOP = ResourceKey.create(
            DataPackRegistryHandler.REGION_REGISTRY_KEY,
            UnshatteredUtils.getUnshatteredIdentifier("jerry_workshop")
    );
}
