package io.github.moosyu.entities.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.moosyu.entities.models.HomingBeeModel;
import io.github.moosyu.entities.projectiles.HomingBee;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.BeeRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

// shoutout BeeRenderer
public class HomingBeeRenderer extends EntityRenderer<HomingBee, BeeRenderState> {
    private static final Identifier ANGRY_BEE_TEXTURE = Identifier.withDefaultNamespace("textures/entity/bee/bee_angry.png");
    private static final float BODY_CENTER_Y = 0.3F;
    private final HomingBeeModel model;

    public HomingBeeRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new HomingBeeModel(context.bakeLayer(ModelLayers.BEE));
        this.shadowRadius = 0.2F;
    }

    @Override
    public @NonNull BeeRenderState createRenderState() {
        return new BeeRenderState();
    }

    @Override
    public void extractRenderState(@NonNull HomingBee entity, @NonNull BeeRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.xRot = entity.getXRot(partialTick);
        state.yRot = entity.getYRot(partialTick);
        state.isAngry = true;
        state.isOnGround = false;
        state.hasStinger = true;
        state.rollAmount = 0.0F;
    }

    @Override
    public void submit(BeeRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, @NonNull CameraRenderState camera) {
        poseStack.pushPose();

        poseStack.translate(0.0F, BODY_CENTER_Y, 0.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - state.yRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(state.xRot));
        poseStack.translate(0.0F, -BODY_CENTER_Y, 0.0F);
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.translate(0.0F, -1.501F, 0.0F);

        RenderType renderType = this.model.renderType(ANGRY_BEE_TEXTURE);
        submitNodeCollector.submitModel(this.model, state, poseStack, renderType, state.lightCoords, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF, null, state.outlineColor, (ModelFeatureRenderer.CrumblingOverlay) null);

        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }
}