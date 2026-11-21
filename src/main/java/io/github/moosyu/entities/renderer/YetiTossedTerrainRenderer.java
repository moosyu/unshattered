package io.github.moosyu.entities.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.moosyu.entities.projectile.terrain.TossedTerrain;
import io.github.moosyu.entities.projectile.terrain.YetiTossShape;
import io.github.moosyu.entities.states.TossedTerrainRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.MovingBlockRenderState;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class YetiTossedTerrainRenderer extends EntityRenderer<TossedTerrain, TossedTerrainRenderState> {
    // i think this is the centre?
    private static final double PIVOT_Y = 1.5;
    private static final float RADIANS_PER_TICK = 0.3f;

    public YetiTossedTerrainRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public @NonNull TossedTerrainRenderState createRenderState() {
        return new TossedTerrainRenderState();
    }

    @Override
    public void extractRenderState(@NonNull TossedTerrain tossedTerrain, @NonNull TossedTerrainRenderState state, float partialTicks) {
        super.extractRenderState(tossedTerrain, state, partialTicks);

        BlockPos blockPos = BlockPos.containing(tossedTerrain.getX(), tossedTerrain.getBoundingBox().maxY, tossedTerrain.getZ());
        List<BlockState> states = tossedTerrain.getBlockStates();
        Vec3 velocity = tossedTerrain.getDeltaMovement();
        double horizontalMagnitude = Math.sqrt(Mth.square(velocity.x) + Mth.square(velocity.z));

        if (horizontalMagnitude > 0.01) {
            state.dirX = (float) (velocity.x / horizontalMagnitude);
            state.dirZ = (float) (velocity.z / horizontalMagnitude);
        } else {
            // fallback for looking straight up or down
            state.dirX = 0;
            state.dirZ = 1;
        }

        state.blocks.clear();
        for (int i = 0; i < states.size(); i++) {
            MovingBlockRenderState block = new MovingBlockRenderState();
            // rotation gets weird as it moves as the seed will keep changing, this sets a static one
            block.randomSeedPos = new BlockPos(tossedTerrain.getId(), i, 0);
            block.blockPos = blockPos;
            block.blockState = states.get(i);
            block.biome = tossedTerrain.level().getBiome(blockPos);
            block.cardinalLighting = tossedTerrain.level().dimensionType().cardinalLightType().get();
            block.lightEngine = tossedTerrain.level().getLightEngine();
            state.blocks.add(block);
        }
    }

    @Override
    public void submit(TossedTerrainRenderState state, @NonNull PoseStack poseStack, @NonNull SubmitNodeCollector collector, @NonNull CameraRenderState camera) {
        // data hasnt been synced yet if this is true
        if (state.blocks.size() != YetiTossShape.OFFSETS.size()) {
            return;
        }

        poseStack.pushPose();

        poseStack.translate(0, PIVOT_Y, 0);

        float angle = state.ageInTicks * RADIANS_PER_TICK;
        poseStack.mulPose(new Quaternionf().rotationAxis(angle, state.dirZ, 0, -state.dirX));

        poseStack.translate(0, -PIVOT_Y, 0);

        for (int i = 0; i < YetiTossShape.OFFSETS.size(); i++) {
            BlockPos offset = YetiTossShape.OFFSETS.get(i);

            poseStack.pushPose();
            poseStack.translate(offset.getX() - 0.5, offset.getY() + 2, offset.getZ() - 0.5);
            collector.submitMovingBlock(poseStack, state.blocks.get(i));
            poseStack.popPose();
        }

        poseStack.popPose();

        super.submit(state, poseStack, collector, camera);
    }
}