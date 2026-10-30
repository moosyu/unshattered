package io.github.moosyu.entities.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.moosyu.entities.models.BaseQuadrupedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public class OasisSheepWoolUndercoatLayer extends RenderLayer<LivingEntityRenderState, BaseQuadrupedModel> {
    private static final Identifier SHEEP_WOOL_UNDERCOAT_LOCATION = Identifier.withDefaultNamespace("textures/entity/sheep/sheep_wool_undercoat.png");
    private final EntityModel<LivingEntityRenderState> model;

    public OasisSheepWoolUndercoatLayer(RenderLayerParent<LivingEntityRenderState, BaseQuadrupedModel> renderer, EntityModelSet modelSet) {
        super(renderer);
        model = new BaseQuadrupedModel(modelSet.bakeLayer(ModelLayers.SHEEP_WOOL_UNDERCOAT));
    }

    public void submit(@NonNull PoseStack poseStack, @NonNull SubmitNodeCollector submitNodeCollector, int lightCoords, @NonNull LivingEntityRenderState state, float yRot, float xRot) {
        submitNodeCollector.order(1).submitModel(model, state, poseStack, RenderTypes.entityCutout(SHEEP_WOOL_UNDERCOAT_LOCATION), lightCoords, LivingEntityRenderer.getOverlayCoords(state, 0.0f), 0xFFFFFFFF, null, state.outlineColor, null);
    }
}