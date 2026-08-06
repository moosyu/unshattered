package io.github.moosyu.events;

import io.github.moosyu.data.regions.Region;
import io.github.moosyu.data.regions.RegionBoundary;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class DataPackRegistryHandler {
    public static final ResourceKey<Registry<Region>> REGION_REGISTRY_KEY = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(MODID, "regions"));
    public static final ResourceKey<Registry<RegionBoundary>> REGION_BOUNDARY_REGISTRY_KEY = ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(MODID, "region_boundaries"));

    @SubscribeEvent
    public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(
                REGION_REGISTRY_KEY,
                Region.RECORD_CODEC,
                Region.RECORD_CODEC
        );

        event.dataPackRegistry(
                REGION_BOUNDARY_REGISTRY_KEY,
                RegionBoundary.CODEC
        );
    }
}
