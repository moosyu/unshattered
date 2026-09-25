package io.github.moosyu.events;

import io.github.moosyu.Unshattered;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.regen.RegenPaths;
import io.github.moosyu.data.regions.BoundaryCoordinates;
import io.github.moosyu.data.regions.RegionAreas;
import io.github.moosyu.data.regions.UnshatteredRegions;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.joml.Vector2i;

import java.util.stream.Collectors;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.events.DataPackRegistryHandler.REGEN_PATH_REGISTRY_KEY;
import static io.github.moosyu.events.DataPackRegistryHandler.REGION_BOUNDARY_REGISTRY_KEY;
import static net.minecraft.world.level.Level.OVERWORLD;

@EventBusSubscriber(modid = MODID)
public class ServerStartingHandler {
    @SubscribeEvent
    public static void onServerStart(ServerStartingEvent event) {
        MinecraftServer server = event.getServer();
        Unshattered.LOGGER.info("HELLO from server starting");
        UnshatteredAttributeValues.buildLookup();
        ServerLevel serverLevel = server.getLevel(OVERWORLD);
        BoundaryCoordinates boundaryCoordinates = BoundaryCoordinates.getRegionCoordinates(serverLevel, UnshatteredRegions.DEFAULT_REGION);

        if (serverLevel == null) return;

        RegistryAccess registryAccess = serverLevel.registryAccess();
        if (boundaryCoordinates != null) {
            Vector2i boundaryCoordinatesLength = boundaryCoordinates.getRectangleLengths();
            RegionAreas.createRegionAreaGrid(new Vector2i(boundaryCoordinatesLength.x, boundaryCoordinatesLength.y), registryAccess.lookupOrThrow(REGION_BOUNDARY_REGISTRY_KEY).stream().toList());
        }

        RegenPaths.REGEN_IDENTIFIER_BY_BLOCK = registryAccess.lookupOrThrow(REGEN_PATH_REGISTRY_KEY)
                .listElements()
                .collect(Collectors.toMap(ref -> ref.value().path().getFirst(),
                        ref -> ref.key().identifier()
                ));
    }
}
