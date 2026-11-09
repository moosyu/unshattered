package io.github.moosyu.entities.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.moosyu.entities.decoration.present.Present;
import io.github.moosyu.entities.models.PresentModel;
import io.github.moosyu.entities.states.PresentRenderState;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import org.jspecify.annotations.NonNull;

public class PresentRenderer extends EntityRenderer<Present, PresentRenderState> {
    private final PresentModel model;

    public PresentRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new PresentModel(context.bakeLayer(PresentModel.LAYER_LOCATION));
        this.shadowRadius = 0;
    }

    @Override
    public @NonNull PresentRenderState createRenderState() {
        return new PresentRenderState();
    }

    @Override
    public void extractRenderState(@NonNull Present present, @NonNull PresentRenderState state, float partialTick) {
        super.extractRenderState(present, state, partialTick);
        state.progress = Mth.clamp((present.tickCount + partialTick) / Present.DURATION, 0, 1);
        state.hRot = Mth.square(state.progress) * 8 * Mth.TWO_PI;
        state.liftHeight = Present.getLiftHeight(state.progress);
        state.presentTexturePath = present.getEntityData().get(Present.TEXTURE_PATH);
    }

    @Override
    public void submit(@NonNull PresentRenderState state, PoseStack poseStack, SubmitNodeCollector collector, @NonNull CameraRenderState cameraState) {
        poseStack.pushPose();
        poseStack.scale(-1, -1, 1F);
        poseStack.translate(0, -1.501, 0);
        collector.submitModel(
                model,
                state,
                poseStack,
                RenderTypes.entityCutout(UnshatteredUtils.getUnshatteredIdentifier(state.presentTexturePath)),
                state.lightCoords,
                OverlayTexture.NO_OVERLAY,
                state.outlineColor,
                null
        );
        poseStack.popPose();
        super.submit(state, poseStack, collector, cameraState);
    }

    @Override
    protected @NonNull AABB getBoundingBoxForCulling(Present entity) {
        // it needs to be the entire height of the path or it goes invisible when youre looking at it high enough
        return entity.getBoundingBox().expandTowards(0, Present.MAX_HEIGHT, 0).inflate(1.0);
    }
}