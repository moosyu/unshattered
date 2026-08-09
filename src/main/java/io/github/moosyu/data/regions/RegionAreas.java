package io.github.moosyu.data.regions;

import io.github.moosyu.attachments.PlayerRegionAttachment;
import io.github.moosyu.attachments.UnshatteredAttachments;
import io.github.moosyu.events.DatagenHandler;
import net.minecraft.world.entity.player.Player;
import org.joml.Vector2i;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public final class RegionAreas {
    public static final int REGION_CHUNK_SIZE = 256;
    public static List<RegionBoundary>[][] chunks;

    /**
     * creates a grid of 256 block areas and defines whether regions intersect them. should only be generated once.
     * @param worldSize total world size in blocks to create the grid rectangle.
     * @param regionBoundaries regions that should be included in the grid
     */
    public static void createRegionAreaGrid(Vector2i worldSize, List<RegionBoundary> regionBoundaries) {
        final int HORIZONTAL_CHUNKS = (int) Math.ceil((double) worldSize.x / REGION_CHUNK_SIZE);
        final int VERTICAL_CHUNKS = (int) Math.ceil((double) worldSize.y / REGION_CHUNK_SIZE);
        @SuppressWarnings("unchecked")
        List<RegionBoundary>[][] grid = (List<RegionBoundary>[][]) new ArrayList[HORIZONTAL_CHUNKS][VERTICAL_CHUNKS];
        chunks = grid;

        for (int i = 0; i < HORIZONTAL_CHUNKS; i++) {
            for (int j = 0; j < VERTICAL_CHUNKS; j++) {
                chunks[i][j] = new ArrayList<>();

                int chunkX = i * REGION_CHUNK_SIZE;
                int chunkTopY = Math.min((j + 1) * REGION_CHUNK_SIZE, worldSize.y);
                int chunkWidth = Math.min(REGION_CHUNK_SIZE, worldSize.x - chunkX);
                int chunkHeight = chunkTopY - (j * REGION_CHUNK_SIZE);
                Vector2i chunkTopLeft = new Vector2i(chunkX, chunkTopY);

                for (RegionBoundary regionBoundary : regionBoundaries) {
                    BoundaryCoordinates boundaryCoordinates = regionBoundary.boundaryCoordinates();
                    if (checkRectangleOverlap(chunkTopLeft, chunkWidth, chunkHeight, boundaryCoordinates.topLeftCornerCoordinates(), boundaryCoordinates.width(), boundaryCoordinates.height())) {
                        chunks[i][j].add(regionBoundary);
                    }
                }
            }
        }
    }

    public static boolean checkRectangleOverlap(Vector2i aTopLeft, int aWidth, int aHeight, Vector2i bTopLeft, int bWidth, int bHeight) {
        return aTopLeft.x <= bTopLeft.x + bWidth && aTopLeft.x + aWidth >= bTopLeft.x && aTopLeft.y - aHeight <= bTopLeft.y && aTopLeft.y >= bTopLeft.y - bHeight;
    }

    public static boolean isPointInsideRectangle(Vector2i rectangleTopLeft, int rectangleWidth, int rectangleHeight, Vector2i point) {
        return (point.x >= rectangleTopLeft.x && point.x <= rectangleTopLeft.x + rectangleWidth) &&
                (point.y <= rectangleTopLeft.y && point.y >= rectangleTopLeft.y - rectangleHeight);
    }

    @Nullable
    public static Vector2i checkAdjacentChunks() {
        return null;
    }

    public static void updatePlayerRegionAttachment(Player player, RegionBoundary selectedBoundary, Vector2i selectedChunk) {
        if (selectedBoundary == null) return;
        player.setData(UnshatteredAttachments.PLAYER_REGION.get(), new PlayerRegionAttachment(selectedBoundary.region().getKey(), selectedChunk, player.blockPosition()));
    }

    public static void updatePlayerRegion(Player player) {
        PlayerRegionAttachment regionAttachment = player.getData(UnshatteredAttachments.PLAYER_REGION.get());
        RegionBoundary selectedBoundary = null;
        Vector2i selectedChunk = new Vector2i(0, 0);
        if (regionAttachment.currentBlockPos() != player.blockPosition()) {
            // todo: check 8 adjacent tiles if the player isnt inside and if that also doesnt turn up anything check every single one
            // base selected region on highest boundary
            if (isPointInsideRectangle(new Vector2i(
                    regionAttachment.currentChunk().x * REGION_CHUNK_SIZE,
                    regionAttachment.currentChunk().y * REGION_CHUNK_SIZE),
                    REGION_CHUNK_SIZE, REGION_CHUNK_SIZE,
                    new Vector2i(player.getBlockX(), player.getBlockZ()))
            ) {
                selectedChunk = regionAttachment.currentChunk();
                for (RegionBoundary regionBoundary : chunks[regionAttachment.currentChunk().x][regionAttachment.currentChunk().y]) {
                    if (selectedBoundary == null || regionBoundary.boundaryCoordinates().priority() > selectedBoundary.boundaryCoordinates().priority()) {
                        selectedBoundary = regionBoundary;
                    }
                }
                updatePlayerRegionAttachment(player, selectedBoundary, selectedChunk);
                return;
            }
        }
        Vector2i successfulAdjacentChunk = checkAdjacentChunks();
        if (successfulAdjacentChunk != null) {
        }
    }
}