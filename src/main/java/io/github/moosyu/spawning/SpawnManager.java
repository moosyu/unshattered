package io.github.moosyu.spawning;

import io.github.moosyu.data.UnshatteredDataMaps;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.regions.Region;
import io.github.moosyu.data.regions.RegionAreas;
import io.github.moosyu.events.DataPackRegistryHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class SpawnManager {
    private static final int CHECK_INTERVAL = 20;
    private static final int RESPAWN_TIME = 600;
    // spawn points that have spawned an entity already
    private static final Set<BlockPos> ACTIVE_SPAWN_POINTS = new HashSet<>();
    private static final Map<BlockPos, Integer> SPAWN_POINT_COOLDOWNS = new HashMap<>();

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

            if (spawnPoint == null) {
                return;
            }

            BlockPos pos = spawnPoint.blockPos();
            // hasChunkAt is deprecated so we're getting wacky and wild
            if (!serverLevel.getChunkSource().hasChunk(
                    SectionPos.blockToSectionCoord(pos.getX()),
                    SectionPos.blockToSectionCoord(pos.getZ()))
            ) {
                return;
            }

            Integer cooldown = SPAWN_POINT_COOLDOWNS.get(pos);
            if (cooldown != null && cooldown > 0) {
                SPAWN_POINT_COOLDOWNS.put(pos, cooldown - CHECK_INTERVAL);
                return;
            } else if (ACTIVE_SPAWN_POINTS.contains(pos)) {
                return;
            }

            Entity spawnedEntity = spawnPoint.entityType().spawn(serverLevel, pos, EntitySpawnReason.TRIGGERED);
            if (spawnedEntity != null) {
                if (spawnedEntity instanceof LivingEntity livingEntity) {
                    spawnPoint.equipment().forEach((equipmentSlot, itemHolder) -> {
                        livingEntity.setItemSlot(equipmentSlot, new ItemStack(itemHolder));
                    });
                }

                spawnedEntity.setData(UnshatteredAttachments.MOB_SPAWN_BLOCK_POS.get(), pos);
                ACTIVE_SPAWN_POINTS.add(pos);
                SPAWN_POINT_COOLDOWNS.remove(pos);
            }
        });
    }

    @SubscribeEvent
    public static void onJoin(EntityJoinLevelEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level)
                || level.isClientSide()
                || !(event.getEntity() instanceof Mob mob)
        ) {
            return;
        }

        if (mob.hasData(UnshatteredAttachments.MOB_SPAWN_BLOCK_POS.get())) {
            event.setCanceled(true);
        }
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
            BlockPos spawnPos = mob.getData(UnshatteredAttachments.MOB_SPAWN_BLOCK_POS.get());
            ACTIVE_SPAWN_POINTS.remove(spawnPos);

            Entity.RemovalReason reason = mob.getRemovalReason();
            if (reason != null && reason.shouldDestroy()) {
                SPAWN_POINT_COOLDOWNS.put(spawnPos, RESPAWN_TIME);
            }
        }
    }

    @SubscribeEvent
    public static void onServerStopped(ServerStoppedEvent event) {
        ACTIVE_SPAWN_POINTS.clear();
        SPAWN_POINT_COOLDOWNS.clear();
    }
}