package io.github.moosyu.abilities.data;

import io.github.moosyu.abilities.AbilityContext;
import io.github.moosyu.abilities.ActiveAbility;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.entities.UnshatteredEntities;
import io.github.moosyu.entities.projectile.terrain.TossedTerrain;
import io.github.moosyu.entities.projectile.terrain.YetiTossShape;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class TerrainToss extends ActiveAbility {
    private static final double MAX_RANGE = 24.0;
    // blocks/tick horizontally
    private static final double H_SPEED = 1.2;
    private static final int MIN_TICKS = 6;
    private static final int MAX_TICKS = 40;

    public TerrainToss() {
        // update mana to 40 when done testing
        super(new ItemAbility(40, 20, 0));
    }

    @Override
    protected void onActivate(AbilityContext context, boolean refreshing) {
        context.getServerPlayer().ifPresent(player -> {
            Level level = player.level();

            BlockState blockStateCenter = null;
            BlockPos.MutableBlockPos blockPosCenter = player.blockPosition().below().mutable();
            // so if the player is jumping or falling close to the ground they can still throw
            for (int i = 0; i < 4; i++) {
                if (!level.getBlockState(blockPosCenter).getCollisionShape(level, blockPosCenter).isEmpty()) {
                    blockStateCenter = level.getBlockState(blockPosCenter);
                    break;
                }

                blockPosCenter.move(Direction.DOWN);
            }

            if (blockStateCenter == null) {
                return;
            }

            // EntityDataAccessor works better with lists than arrays
            List<BlockState> sampled = new ArrayList<>(YetiTossShape.OFFSETS.size());

            // had an array earlier but here we are i guess
            for (int i = 0; i < YetiTossShape.OFFSETS.size(); i++) {
                BlockState sample = level.getBlockState(blockPosCenter.offset(YetiTossShape.OFFSETS.get(i)));
                if (sample.getRenderShape() == RenderShape.MODEL) {
                    sampled.add(sample);
                } else {
                    sampled.add(Blocks.AIR.defaultBlockState());
                }
            }

            TossedTerrain tossedTerrain = new TossedTerrain(UnshatteredEntities.YETI_TOSSED_TERRAIN.get(), level);
            tossedTerrain.setOwner(player);
            tossedTerrain.setBlockStates(sampled);

            Vec3 eye = player.getEyePosition();
            Vec3 startPosition = new Vec3(eye.x, eye.y + 1.5, eye.z);
            tossedTerrain.setPos(startPosition.x, startPosition.y, startPosition.z);

            HitResult hit = player.pick(MAX_RANGE, 1.0f, false);
            // if the ray never hit anything this is just max range
            Vec3 target = hit.getLocation();

            Vec3 displacement = target.subtract(startPosition);
            Vec3 horizontal = new Vec3(displacement.x, 0, displacement.z);
            double horizontalDist = horizontal.length();
            int flightTime = Mth.clamp((int) Math.ceil(horizontalDist / H_SPEED), MIN_TICKS, MAX_TICKS);
            Vec3 vx = horizontalDist > 1.0E-6 ? horizontal.scale(1.0 / flightTime) : Vec3.ZERO;
            double vy = (displacement.y + TossedTerrain.GRAVITY * flightTime * (flightTime - 1) / 2.0) / flightTime;

            tossedTerrain.setDeltaMovement(vx.x, vy, vx.z);
            level.addFreshEntity(tossedTerrain);
        });
    }
}
