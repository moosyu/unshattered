package io.github.moosyu.data.regions;

import io.github.moosyu.data.attachments.PlayerRegionAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
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
                final int CHUNK_X = worldMinX + i * REGION_CHUNK_SIZE;
                final int CHUNK_Z = worldMinZ + j * REGION_CHUNK_SIZE;
                final int CHUNK_WIDTH = Math.min(REGION_CHUNK_SIZE, worldSize.x - i * REGION_CHUNK_SIZE);
                final int CHUNK_HEIGHT = Math.min(REGION_CHUNK_SIZE, worldSize.y - j * REGION_CHUNK_SIZE);
                final Vector2i CHUNK_TOP_LEFT = new Vector2i(CHUNK_X, CHUNK_Z);
                final Vector2i CHUNK_BOTTOM_RIGHT = new Vector2i(CHUNK_X + CHUNK_WIDTH, CHUNK_Z + CHUNK_HEIGHT);

                for (RegionBoundary regionBoundary : regionBoundaries) {
                    if (regionBoundary.region().getKey() == UnshatteredRegions.DEFAULT_REGION) continue;
                    BoundaryCoordinates boundaryCoordinates = regionBoundary.boundaryCoordinates();

                    if (checkRectangleOverlap(
                            CHUNK_TOP_LEFT,
                            CHUNK_BOTTOM_RIGHT,
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

    public static boolean containsPoint(BoundaryCoordinates boundaryCoordinates, BlockPos blockPos) {
        final int X = blockPos.getX();
        final int Y = blockPos.getY();
        final int Z = blockPos.getZ();

        return Y >= boundaryCoordinates.minY() && Y <= boundaryCoordinates.maxY()
                && X >= Math.min(boundaryCoordinates.topLeftCornerCoordinates().x, boundaryCoordinates.bottomRightCornerCoordinates().x)
                && X <= Math.max(boundaryCoordinates.topLeftCornerCoordinates().x, boundaryCoordinates.bottomRightCornerCoordinates().x)
                && Z >= Math.min(boundaryCoordinates.topLeftCornerCoordinates().y, boundaryCoordinates.bottomRightCornerCoordinates().y)
                && Z <= Math.max(boundaryCoordinates.topLeftCornerCoordinates().y, boundaryCoordinates.bottomRightCornerCoordinates().y
        );
    }

    public static void updatePlayerRegion(Player player) {
        final BlockPos BLOCK_POS = player.blockPosition();
        if (player.getData(UnshatteredAttachments.PLAYER_REGION.get()).currentBlockPos().equals(BLOCK_POS)) {
            return;
        }

        final int CHUNK_X = Mth.clamp(Math.floorDiv(BLOCK_POS.getX() - worldMinX, REGION_CHUNK_SIZE), 0, chunks.length - 1);
        final int CHUNK_Z = Mth.clamp(Math.floorDiv(BLOCK_POS.getZ() - worldMinZ, REGION_CHUNK_SIZE), 0, chunks[0].length - 1);
        RegionBoundary selectedBoundary = null;

        for (RegionBoundary regionBoundary : chunks[CHUNK_X][CHUNK_Z]) {
            BoundaryCoordinates boundaryCoordinates = regionBoundary.boundaryCoordinates();

            if (containsPoint(boundaryCoordinates, BLOCK_POS)
                    && (selectedBoundary == null || boundaryCoordinates.priority() > selectedBoundary.boundaryCoordinates().priority())
            ) {
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
        final int SEARCH_BLOCKS_RADIUS = player.level().getServer().getPlayerList().getSimulationDistance() * 16;
        final Vector2i SEARCH_TOP_LEFT = new Vector2i(player.blockPosition().getX() - SEARCH_BLOCKS_RADIUS, player.blockPosition().getZ() - SEARCH_BLOCKS_RADIUS);
        final Vector2i SEARCH_BOTTOM_RIGHT = new Vector2i(player.blockPosition().getX() + SEARCH_BLOCKS_RADIUS, player.blockPosition().getZ() + SEARCH_BLOCKS_RADIUS);

        Set<RegionBoundary> candidates = new HashSet<>();
        for (int i = Mth.clamp(Math.floorDiv(SEARCH_TOP_LEFT.x - worldMinX, REGION_CHUNK_SIZE), 0, chunks.length - 1);
             i <= Mth.clamp(Math.floorDiv(SEARCH_BOTTOM_RIGHT.x - worldMinX, REGION_CHUNK_SIZE), 0, chunks.length - 1);
             i++
        ) {
            for (int j = Mth.clamp(Math.floorDiv(SEARCH_TOP_LEFT.y - worldMinZ, REGION_CHUNK_SIZE), 0, chunks[0].length - 1);
                 j <= Mth.clamp(Math.floorDiv(SEARCH_BOTTOM_RIGHT.y - worldMinZ, REGION_CHUNK_SIZE), 0, chunks[0].length - 1);
                 j++
            ) {
                candidates.addAll(chunks[i][j]);
            }
        }

        final Set<Region> REGIONS = new HashSet<>();
        REGIONS.add(defaultRegion);
        for (RegionBoundary boundary : candidates) {
            final BoundaryCoordinates BOUNDARY_COORDINATES = boundary.boundaryCoordinates();

            if (BOUNDARY_COORDINATES.minY() <= (player.blockPosition().getY() + VERTICAL_RADIUS)
                    && BOUNDARY_COORDINATES.maxY() >= (player.blockPosition().getY() - VERTICAL_RADIUS)
                    && checkRectangleOverlap(SEARCH_TOP_LEFT,
                    SEARCH_BOTTOM_RIGHT,
                    BOUNDARY_COORDINATES.topLeftCornerCoordinates(),
                    BOUNDARY_COORDINATES.bottomRightCornerCoordinates()
            )) {
                REGIONS.add(boundary.region().value());
            }
        }

        return REGIONS;
    }

    public static boolean isBlockPosInRegion(ServerLevel serverLevel, BlockPos blockPos, ResourceKey<Region> regionKey) {
        if (regionKey == UnshatteredRegions.DEFAULT_REGION) {
            return true;
        }

        final BoundaryCoordinates BOUNDARY_COORDINATES = BoundaryCoordinates.getRegionCoordinates(serverLevel, regionKey);
        return BOUNDARY_COORDINATES != null && containsPoint(BOUNDARY_COORDINATES, blockPos);
    }
}