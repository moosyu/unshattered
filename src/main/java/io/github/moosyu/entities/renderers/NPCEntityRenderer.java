package io.github.moosyu.entities.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.moosyu.entities.NPCEntity;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.jspecify.annotations.NonNull;

public class NPCEntityRenderer extends EntityRenderer<NPCEntity, AvatarRenderState> {
    private final Identifier texture;
    private final PlayerModel model;

    public NPCEntityRenderer(EntityRendererProvider.Context context, String skinName) {
        this(context, skinName, false);
    }

    public NPCEntityRenderer(EntityRendererProvider.Context context, String skinName, boolean slim) {
        super(context);

        this.texture = UnshatteredUtils.getUnshatteredIdentifier("textures/entity/npcs/" + skinName + ".png");
        this.model = new PlayerModel(
                context.bakeLayer(slim ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER),
                slim
        );
    }

    @Override
    public @NonNull AvatarRenderState createRenderState() {
        return new AvatarRenderState();
    }

    @Override
    public void extractRenderState(@NonNull NPCEntity entity, @NonNull AvatarRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);

        state.bodyRot = Mth.rotLerp(partialTick, entity.yRotO, entity.getYRot());
        state.yRot = 0.0f;
        state.xRot = Mth.lerp(partialTick, entity.xRotO, entity.getXRot());
        state.showHat = true;
        state.showJacket = true;
        state.showLeftSleeve = true;
        state.showRightSleeve = true;
        state.showLeftPants = true;
        state.showRightPants = true;
    }

    @Override
    public void submit(AvatarRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotationDegrees(180.0f - state.bodyRot));
        poseStack.scale(-1.0f, -1.0f, 1.0f);
        poseStack.translate(0.0f, -1.501f, 0.0f);

        collector.submitModel(
                model,
                state,
                poseStack,
                model.renderType(texture),
                state.lightCoords,
                OverlayTexture.NO_OVERLAY,
                0xFFFFFFFF,
                null,
                state.outlineColor,
                null
        );

        poseStack.popPose();
        super.submit(state, poseStack, collector, camera);
    }
}