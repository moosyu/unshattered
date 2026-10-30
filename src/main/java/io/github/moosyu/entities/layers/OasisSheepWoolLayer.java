package io.github.moosyu.entities.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.moosyu.entities.models.BaseQuadrupedModel;
import io.github.moosyu.util.UnshatteredUtils;
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

public class OasisSheepWoolLayer extends RenderLayer<LivingEntityRenderState, BaseQuadrupedModel> {
    private static final Identifier SHEEP_WOOL_LOCATION = UnshatteredUtils.getUnshatteredIdentifier("textures/entity/mobs/oasis_sheep_wool.png");
    private final BaseQuadrupedModel adultModel;

    public OasisSheepWoolLayer(RenderLayerParent<LivingEntityRenderState, BaseQuadrupedModel> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.adultModel = new BaseQuadrupedModel(modelSet.bakeLayer(ModelLayers.SHEEP_WOOL));
    }

    @Override
    public void submit(@NonNull PoseStack poseStack, @NonNull SubmitNodeCollector submitNodeCollector, int lightCoords, LivingEntityRenderState state, float yRot, float xRot) {
        if (state.isInvisible) {
            if (state.appearsGlowing()) {
                submitNodeCollector.submitModel(
                        adultModel,
                        state,
                        poseStack,
                        RenderTypes.outline(SHEEP_WOOL_LOCATION),
                        lightCoords,
                        LivingEntityRenderer.getOverlayCoords(state, 0.0F),
                        -16777216,
                        null,
                        state.outlineColor,
                        null
                );
            }
        } else {
            submitNodeCollector.order(1).submitModel(
                    adultModel,
                    state,
                    poseStack,
                    RenderTypes.entityCutout(SHEEP_WOOL_LOCATION),
                    lightCoords,
                    LivingEntityRenderer.getOverlayCoords(state, 0.0F),
                    0xFFFFFFFF,
                    null,
                    state.outlineColor,
                    null
            );
        }
    }
}