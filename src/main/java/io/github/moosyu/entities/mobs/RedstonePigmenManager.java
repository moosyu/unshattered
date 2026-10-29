package io.github.moosyu.entities.mobs;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.common.EventBusSubscriber;
import org.jspecify.annotations.Nullable;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class RedstonePigmenManager {
    @Nullable public static ResourceKey<Level> targetDimension;
    /**
     * the location of the pigman making the call, not the player
     */
    @Nullable public static Vec3 targetPos;
    public static long targetSetTime;

    public static void tick(ServerLevel level) {
        if (targetPos != null && level.getGameTime() - targetSetTime > 600) {
            clearTarget();
        }
    }
    public static void setTarget(ServerLevel level, Vec3 pigmanPos) {
        targetDimension = level.dimension();
        targetPos = pigmanPos;
        targetSetTime = level.getGameTime();
    }

    public static void clearTarget() {
        targetPos = null;
        targetDimension = null;
    }
}