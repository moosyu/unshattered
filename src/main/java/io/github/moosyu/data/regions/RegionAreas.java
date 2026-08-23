package io.github.moosyu.data.regions;

import io.github.moosyu.data.attachments.PlayerRegionAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;

public final class RegionAreas {
    public static final int REGION_CHUNK_SIZE = 256;
    public static List<RegionBoundary>[][] chunks;
    public static int worldMinX;
    public static int worldMinZ;
    /**
     * creates a grid of 256 block areas and defines whether regions intersect them. should only be generated once.
     * @param worldSize total world size in blocks to create the grid rectangle.
     * @param regionBoundaries regions that should be included in the grid
     */
    public static void createRegionAreaGrid(Vector2i worldSize, List<RegionBoundary> regionBoundaries) {
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

    public static boolean containsPoint(BoundaryCoordinates boundaryCoordinates, int x, int z) {
        return x >= Math.min(boundaryCoordinates.topLeftCornerCoordinates().x, boundaryCoordinates.bottomRightCornerCoordinates().x)
                && x <= Math.max(boundaryCoordinates.topLeftCornerCoordinates().x, boundaryCoordinates.bottomRightCornerCoordinates().x)
                && z >= Math.min(boundaryCoordinates.topLeftCornerCoordinates().y, boundaryCoordinates.bottomRightCornerCoordinates().y)
                && z <= Math.max(boundaryCoordinates.topLeftCornerCoordinates().y, boundaryCoordinates.bottomRightCornerCoordinates().y);
    }

    public static void updatePlayerRegion(Player player) {
        if (player.getData(UnshatteredAttachments.PLAYER_REGION.get()).currentBlockPos().getX() == player.blockPosition().getX()
                && player.getData(UnshatteredAttachments.PLAYER_REGION.get()).currentBlockPos().getZ() == player.blockPosition().getZ()
        ) return;

        int blockX = player.getBlockX();
        int blockZ = player.getBlockZ();
        int chunkX = Mth.clamp(Math.floorDiv(blockX - worldMinX, REGION_CHUNK_SIZE), 0, chunks.length - 1);
        int chunkZ = Mth.clamp(Math.floorDiv(blockZ - worldMinZ, REGION_CHUNK_SIZE), 0, chunks[0].length - 1);
        RegionBoundary selectedBoundary = null;

        for (RegionBoundary regionBoundary : chunks[chunkX][chunkZ]) {
            if (!containsPoint(regionBoundary.boundaryCoordinates(), blockX, blockZ)) continue;
            if (selectedBoundary == null || regionBoundary.boundaryCoordinates().priority() > selectedBoundary.boundaryCoordinates().priority()) {
                selectedBoundary = regionBoundary;
            }
        }

        updatePlayerRegionAttachment(player, selectedBoundary);
    }
}