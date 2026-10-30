package io.github.moosyu.entities.renderers;

import io.github.moosyu.entities.layers.OasisSheepWoolLayer;
import io.github.moosyu.entities.layers.OasisSheepWoolUndercoatLayer;
import io.github.moosyu.entities.mobs.OasisSheep;
import io.github.moosyu.entities.models.BaseQuadrupedModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public class OasisSheepRenderer extends MobRenderer<OasisSheep, LivingEntityRenderState, BaseQuadrupedModel> {
    private static final Identifier SHEEP_LOCATION = Identifier.withDefaultNamespace("textures/entity/sheep/sheep.png");

    public OasisSheepRenderer(EntityRendererProvider.Context context) {
        super(context, new BaseQuadrupedModel(context.bakeLayer(ModelLayers.SHEEP)), 0.7f);
        addLayer(new OasisSheepWoolUndercoatLayer(this, context.getModelSet()));
        addLayer(new OasisSheepWoolLayer(this, context.getModelSet()));
    }

    @Override
    public @NonNull Identifier getTextureLocation(@NonNull LivingEntityRenderState renderState) {
        return SHEEP_LOCATION;
    }

    @Override
    public @NonNull LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }
}
