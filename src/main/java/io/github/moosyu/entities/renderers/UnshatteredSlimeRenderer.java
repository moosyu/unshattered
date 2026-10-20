package io.github.moosyu.entities.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.moosyu.entities.mobs.MassiveEmeraldSlime;
import io.github.moosyu.entities.mobs.UnshatteredSlime;
import io.github.moosyu.entities.states.UnshatteredSlimeRenderState;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.monster.slime.SlimeModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public class UnshatteredSlimeRenderer extends MobRenderer<UnshatteredSlime, UnshatteredSlimeRenderState, SlimeModel> {
    private final int size;
    private final Identifier texture;

    public UnshatteredSlimeRenderer(EntityRendererProvider.Context context, int size, Identifier texture) {
        super(context, new SlimeModel(context.bakeLayer(ModelLayers.SLIME)), 0.25f);
        this.size = size;
        addLayer(new UnshatteredSlimeOuterLayer(this, context.getModelSet()));
        this.texture = texture;
    }

    @Override
    public @NonNull Identifier getTextureLocation(@NonNull UnshatteredSlimeRenderState state) {
        return texture;
    }

    @Override
    public @NonNull UnshatteredSlimeRenderState createRenderState() {
        return new UnshatteredSlimeRenderState();
    }

    @Override
    public void extractRenderState(@NonNull UnshatteredSlime slime, @NonNull UnshatteredSlimeRenderState state, float partialTicks) {
        super.extractRenderState(slime, state, partialTicks);
        state.size = size;
        state.squish = 0.0f;
        state.scaleMultiplier = slime instanceof MassiveEmeraldSlime massive ? massive.getRenderScaleMultiplier(partialTicks) : 1.0f;
    }

    @Override
    public void submit(UnshatteredSlimeRenderState state, @NonNull PoseStack poseStack, @NonNull SubmitNodeCollector collector, @NonNull CameraRenderState camera) {
        this.shadowRadius = 0.25f * state.size;
        super.submit(state, poseStack, collector, camera);
    }

    @Override
    protected void scale(UnshatteredSlimeRenderState state, @NonNull PoseStack poseStack) {
        poseStack.scale(0.999f, 0.999f, 0.999f);
        poseStack.translate(0.0f, 0.001f, 0.0f);

        float size = state.size;
        float scaleFraction = 1.0f / (state.squish / (size * 0.5f + 1.0f) + 1.0f);

        size *= state.scaleMultiplier;
        poseStack.scale(scaleFraction * size, 1.0f / scaleFraction * size, scaleFraction * size);
    }

    public static class UnshatteredSlimeOuterLayer extends RenderLayer<UnshatteredSlimeRenderState, SlimeModel> {
        private final SlimeModel model;

        public UnshatteredSlimeOuterLayer(RenderLayerParent<UnshatteredSlimeRenderState, SlimeModel> renderer, EntityModelSet modelSet) {
            super(renderer);
            model = new SlimeModel(modelSet.bakeLayer(ModelLayers.SLIME_OUTER));
        }

        public void submit(@NonNull PoseStack poseStack, @NonNull SubmitNodeCollector submitNodeCollector, int lightCoords, UnshatteredSlimeRenderState state, float yRot, float xRot) {
            boolean appearsGlowingWithInvisibility = state.appearsGlowing() && state.isInvisible;
            if (!state.isInvisible || appearsGlowingWithInvisibility) {
                int overlayCoords = LivingEntityRenderer.getOverlayCoords(state, 0.0f);
                if (appearsGlowingWithInvisibility) {
                    submitNodeCollector.order(1).submitModel(model, state, poseStack, RenderTypes.outline(SlimeRenderer.SLIME_LOCATION), lightCoords, overlayCoords, state.outlineColor, null);
                } else {
                    submitNodeCollector.order(1).submitModel(model, state, poseStack, RenderTypes.entityTranslucent(SlimeRenderer.SLIME_LOCATION), lightCoords, overlayCoords, state.outlineColor, null);
                }
            }
        }
    }
}