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
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.jspecify.annotations.NonNull;

public class NPCEntityRenderer extends EntityRenderer<NPCEntity, NPCEntityRenderer.NPCRenderState> {
    private final Identifier texture;
    private final PlayerModel model;

    public NPCEntityRenderer(EntityRendererProvider.Context context, String skinName) {
        this(context, skinName, false);
    }

    public NPCEntityRenderer(EntityRendererProvider.Context context, String skinName, boolean slim) {
        super(context);

        texture = UnshatteredUtils.getUnshatteredIdentifier("textures/entity/npcs/" + skinName + ".png");
        model = new PlayerModel(
                context.bakeLayer(slim ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER),
                slim
        );
    }

    @Override
    public @NonNull NPCRenderState createRenderState() {
        return new NPCRenderState();
    }

    @Override
    public void extractRenderState(@NonNull NPCEntity entity, @NonNull NPCRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);

        AvatarRenderState avatar = state.avatar;
        avatar.ageInTicks = state.ageInTicks;
        avatar.bodyRot = Mth.rotLerp(partialTick, entity.yRotO, entity.getYRot());
        avatar.yRot = Mth.lerp(partialTick, entity.yRotO, entity.getYRot());
        avatar.xRot = Mth.lerp(partialTick, entity.xRotO, entity.getXRot());
        avatar.showHat = true;
        avatar.showJacket = true;
        avatar.showLeftSleeve = true;
        avatar.showRightSleeve = true;
        avatar.showLeftPants = true;
        avatar.showRightPants = true;
    }

    @Override
    public void submit(NPCRenderState state, PoseStack poseStack, SubmitNodeCollector collector, @NonNull CameraRenderState camera) {
        AvatarRenderState avatar = state.avatar;

        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotationDegrees(180.0f - avatar.bodyRot));
        poseStack.scale(-1.0f, -1.0f, 1.0f);
        poseStack.translate(0.0f, -1.501f, 0.0f);
        model.setupAnim(avatar);

        collector.submitModel(
                model,
                avatar,
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

    // getRenderer in EntityRenderDispatcher doesn't work if AvatarRenderState is used normally
    public static class NPCRenderState extends EntityRenderState {
        public final AvatarRenderState avatar = new AvatarRenderState();
    }
}