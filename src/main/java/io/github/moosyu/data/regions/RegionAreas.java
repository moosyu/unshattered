package io.github.moosyu.data.regions;

import io.github.moosyu.data.attachments.PlayerRegionAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.joml.Vector2i;

import java.util.*;

public final class RegionAreas {
    public static final int REGION_CHUNK_SIZE = 256;
    public static List<RegionBoundary>[][] chunks;
    public static int worldMinX;
    public static int worldMinZ;
    private static Region defaultRegion;
    private static final int VERTICAL_RADIUS = 32;
    /**
     * creates a grid of 256 block areas and defines whether regions intersect them. should only be generated once.
     * @param worldSize total world size in blocks to create the grid rectangle.
     * @param regionBoundaries regions that should be included in the grid
     */
    public static void createRegionAreaGrid(Vector2i worldSize, List<RegionBoundary> regionBoundaries) {
        defaultRegion = null;
        for (RegionBoundary regionBoundary : regionBoundaries) {
            if (regionBoundary.region().getKey() == UnshatteredRegions.DEFAULT_REGION) {
                defaultRegion = regionBoundary.region().value();
                break;
            }
        }

        // because the world needs to be offset as the top left isnt at 0, 0
        worldMinX = -worldSize.x / 2;
        worldMinZ = -worldSize.y / 2;
        final int HORIZONTAL_CHUNKS = (int) Math.ceil((double) worldSize.x / REGION_CHUNK_SIZE);
        final int VERTICAL_CHUNKS = (int) Math.ceil((double) worldSize.y / REGION_CHUNK_SIZE);

        @SuppressWarnings("unchecked")
        List<RegionBoundary>[][] grid = (List<RegionBoundary>[][]) new ArrayList[HORIZONTAL_CHUNKS][VERTICAL_CHUNKS];
        chunks = grid;

        for (int i = 0; i < HORIZONTAL_CHUNKS; i++) {
            for (int j = 0; j < VERTICAL_CHUNKS; j++) {
                chunks[i][j] = new ArrayList<>();
                int chunkX = worldMinX + i * REGION_CHUNK_SIZE;
                int chunkZ = worldMinZ + j * REGION_CHUNK_SIZE;
                int chunkWidth = Math.min(REGION_CHUNK_SIZE, worldSize.x - i * REGION_CHUNK_SIZE);
                int chunkHeight = Math.min(REGION_CHUNK_SIZE, worldSize.y - j * REGION_CHUNK_SIZE);
                Vector2i chunkTopLeft = new Vector2i(chunkX, chunkZ);
                Vector2i chunkBottomRight = new Vector2i(chunkX + chunkWidth, chunkZ + chunkHeight);

                for (RegionBoundary regionBoundary : regionBoundaries) {
                    if (regionBoundary.region().getKey() == UnshatteredRegions.DEFAULT_REGION) continue;
                    BoundaryCoordinates boundaryCoordinates = regionBoundary.boundaryCoordinates();

                    if (checkRectangleOverlap(
                            chunkTopLeft,
                            chunkBottomRight,
                            boundaryCoordinates.topLeftCornerCoordinates(),
                            boundaryCoordinates.bottomRightCornerCoordinates()
                    )) {
                        chunks[i][j].add(regionBoundary);
                    }
                }
            }
        }
    }

    public static void updatePlayerRegionAttachment(Player player, RegionBoundary selectedBoundary) {
        player.setData(UnshatteredAttachments.PLAYER_REGION.get(),
                new PlayerRegionAttachment(selectedBoundary == null ? UnshatteredRegions.DEFAULT_REGION : selectedBoundary.region().getKey(), player.blockPosition())
        );
    }

    public static boolean checkRectangleOverlap(Vector2i aTopLeft, Vector2i aBottomRight, Vector2i bTopLeft, Vector2i bBottomRight) {
        return Math.min(aTopLeft.x, aBottomRight.x) <= Math.max(bTopLeft.x, bBottomRight.x)
                && Math.max(aTopLeft.x, aBottomRight.x) >= Math.min(bTopLeft.x, bBottomRight.x)
                && Math.min(aTopLeft.y, aBottomRight.y) <= Math.max(bTopLeft.y, bBottomRight.y)
                && Math.max(aTopLeft.y, aBottomRight.y) >= Math.min(bTopLeft.y, bBottomRight.y);
    }

    public static boolean containsPoint(BoundaryCoordinates bc, int x, int y, int z) {
        return y >= bc.minY() && y <= bc.maxY()
                && x >= Math.min(bc.topLeftCornerCoordinates().x, bc.bottomRightCornerCoordinates().x)
                && x <= Math.max(bc.topLeftCornerCoordinates().x, bc.bottomRightCornerCoordinates().x)
                && z >= Math.min(bc.topLeftCornerCoordinates().y, bc.bottomRightCornerCoordinates().y)
                && z <= Math.max(bc.topLeftCornerCoordinates().y, bc.bottomRightCornerCoordinates().y);
    }

    public static void updatePlayerRegion(Player player) {
        BlockPos blockPos = player.blockPosition();
        if (player.getData(UnshatteredAttachments.PLAYER_REGION.get()).currentBlockPos().equals(blockPos)) {
            return;
        }

        int blockX = blockPos.getX();
        int blockY = blockPos.getY();
        int blockZ = blockPos.getZ();
        int chunkX = Mth.clamp(Math.floorDiv(blockX - worldMinX, REGION_CHUNK_SIZE), 0, chunks.length - 1);
        int chunkZ = Mth.clamp(Math.floorDiv(blockZ - worldMinZ, REGION_CHUNK_SIZE), 0, chunks[0].length - 1);
        RegionBoundary selectedBoundary = null;

        for (RegionBoundary regionBoundary : chunks[chunkX][chunkZ]) {
            BoundaryCoordinates bc = regionBoundary.boundaryCoordinates();
            if (!containsPoint(bc, blockX, blockY, blockZ)) continue;
            if (selectedBoundary == null || bc.priority() > selectedBoundary.boundaryCoordinates().priority()) {
                selectedBoundary = regionBoundary;
            }
        }

        updatePlayerRegionAttachment(player, selectedBoundary);
    }

    public static Set<Region> getSurroundingRegions(ServerPlayer player) {
        if (chunks == null || player.level().isClientSide()) {
            return Set.of();
        }

        // multiplied by 16 because a chunk is 16 blocks
        int searchBlocksRadius = player.level().getServer().getPlayerList().getSimulationDistance() * 16;
        Vector2i searchTopLeft = new Vector2i(player.blockPosition().getX() - searchBlocksRadius, player.blockPosition().getZ() - searchBlocksRadius);
        Vector2i searchBottomRight = new Vector2i(player.blockPosition().getX() + searchBlocksRadius, player.blockPosition().getZ() + searchBlocksRadius);

        Set<RegionBoundary> candidates = new HashSet<>();
        for (int i = Mth.clamp(Math.floorDiv(searchTopLeft.x - worldMinX, REGION_CHUNK_SIZE), 0, chunks.length - 1);
             i <= Mth.clamp(Math.floorDiv(searchBottomRight.x - worldMinX, REGION_CHUNK_SIZE), 0, chunks.length - 1);
             i++
        ) {
            for (int j = Mth.clamp(Math.floorDiv(searchTopLeft.y - worldMinZ, REGION_CHUNK_SIZE), 0, chunks[0].length - 1);
                 j <= Mth.clamp(Math.floorDiv(searchBottomRight.y - worldMinZ, REGION_CHUNK_SIZE), 0, chunks[0].length - 1);
                 j++
            ) {
                candidates.addAll(chunks[i][j]);
            }
        }

        Set<Region> regions = new HashSet<>();
        regions.add(defaultRegion);
        for (RegionBoundary boundary : candidates) {
            BoundaryCoordinates boundaryCoordinates = boundary.boundaryCoordinates();

            if (boundaryCoordinates.minY() <= (player.blockPosition().getY() + VERTICAL_RADIUS)
                    && boundaryCoordinates.maxY() >= (player.blockPosition().getY() - VERTICAL_RADIUS)
                    && checkRectangleOverlap(searchTopLeft,
                    searchBottomRight,
                    boundaryCoordinates.topLeftCornerCoordinates(),
                    boundaryCoordinates.bottomRightCornerCoordinates()
            )) {
                regions.add(boundary.region().value());
            }
        }

        return regions;
    }
}