package io.github.moosyu.spawning;

import io.github.moosyu.data.UnshatteredDataMaps;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.regions.Region;
import io.github.moosyu.data.regions.RegionAreas;
import io.github.moosyu.events.DataPackRegistryHandler;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Mob;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID, value = Dist.DEDICATED_SERVER)
public class SpawnManager {
    private static final int CHECK_INTERVAL = 20;
    // spawn points that have spawned an entity already
    private static final Set<EntitySpawnPoint> ACTIVE_SPAWN_POINTS = new HashSet<>();
    private static final HashMap<EntitySpawnPoint, Integer> SPAWN_POINT_COOLDOWNS = new HashMap<>();

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        ServerLevel serverLevel = event.getServer().overworld();

        if (serverLevel.getGameTime() % CHECK_INTERVAL != 0) {
            return;
        }

        Set<Region> spawnCheckRegions = new HashSet<>();

        serverLevel.players().forEach(serverPlayer -> spawnCheckRegions.addAll(RegionAreas.getSurroundingRegions(serverPlayer)));
        spawnCheckRegions.forEach(region -> {
            EntitySpawnPoint spawnPoint = serverLevel.registryAccess()
                    .lookupOrThrow(DataPackRegistryHandler.REGION_REGISTRY_KEY)
                    .wrapAsHolder(region)
                    .getData(UnshatteredDataMaps.MOB_SPAWN_DATA);

            // hasChunkAt is deprecated so we're getting wacky and wild
            if (spawnPoint != null && serverLevel.getChunkSource()
                    .hasChunk(SectionPos.blockToSectionCoord(spawnPoint.blockPos().getX()),
                            SectionPos.blockToSectionCoord(spawnPoint.blockPos().getZ())
                    )
            ) {
                Integer cooldown = SPAWN_POINT_COOLDOWNS.get(spawnPoint);
                if (cooldown != null && cooldown > 0) {
                    SPAWN_POINT_COOLDOWNS.put(spawnPoint, cooldown - 1);
                } else {
                    if (!ACTIVE_SPAWN_POINTS.contains(spawnPoint)) {
                        Entity spawnedEntity = spawnPoint.entityType().spawn(serverLevel, spawnPoint.blockPos(), EntitySpawnReason.TRIGGERED);
                        if (spawnedEntity != null) {
                            spawnedEntity.setData(UnshatteredAttachments.MOB_SPAWN_BLOCK_POS.get(), spawnPoint.blockPos());
                            ACTIVE_SPAWN_POINTS.add(spawnPoint);
                            SPAWN_POINT_COOLDOWNS.remove(spawnPoint);
                        }
                    }
                }
            }
        });
    }

    @SubscribeEvent
    public static void onLeave(EntityLeaveLevelEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level)
                || level.isClientSide()
                || !(event.getEntity() instanceof Mob mob)
        ) {
            return;
        }

        if (mob.hasData(UnshatteredAttachments.MOB_SPAWN_BLOCK_POS.get())) {
            
        }
    }

    @SubscribeEvent
    public static void onServerStopped(ServerStoppedEvent event) {
        ACTIVE_SPAWN_POINTS.clear();
        SPAWN_POINT_COOLDOWNS.clear();
    }
}
