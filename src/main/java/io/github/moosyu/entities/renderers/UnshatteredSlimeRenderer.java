package io.github.moosyu.entities.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.moosyu.entities.mobs.UnshatteredSlime;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.monster.slime.SlimeModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.SlimeOuterLayer;
import net.minecraft.client.renderer.entity.state.SlimeRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public class UnshatteredSlimeRenderer extends MobRenderer<UnshatteredSlime, SlimeRenderState, SlimeModel> {
    private final int size;

    public UnshatteredSlimeRenderer(EntityRendererProvider.Context context, int size) {
        super(context, new SlimeModel(context.bakeLayer(ModelLayers.SLIME)), 0.25f);
        this.size = size;
        this.addLayer(new SlimeOuterLayer(this, context.getModelSet()));
    }

    @Override
    public @NonNull Identifier getTextureLocation(@NonNull SlimeRenderState state) {
        return Identifier.withDefaultNamespace("textures/entity/slime/slime.png");
    }

    @Override
    public @NonNull SlimeRenderState createRenderState() {
        return new SlimeRenderState();
    }

    @Override
    public void extractRenderState(@NonNull UnshatteredSlime slime, @NonNull SlimeRenderState state, float partialTicks) {
        super.extractRenderState(slime, state, partialTicks);
        state.size = size;
        state.squish = 0.0f;
    }

    @Override
    public void submit(@NonNull SlimeRenderState state, @NonNull PoseStack poseStack, @NonNull SubmitNodeCollector collector, @NonNull CameraRenderState camera) {
        this.shadowRadius = 0.25f * state.size;
        super.submit(state, poseStack, collector, camera);
    }

    @Override
    protected void scale(@NonNull SlimeRenderState state, @NonNull PoseStack poseStack) {
        poseStack.scale(0.999f, 0.999f, 0.999f);
        poseStack.translate(0.0f, 0.001f, 0.0f);
        float s = state.size;
        float h = state.squish / (s * 0.5f + 1.0f);
        float i = 1.0f / (h + 1.0f);
        poseStack.scale(i * s, 1.0f / i * s, i * s);
    }
}