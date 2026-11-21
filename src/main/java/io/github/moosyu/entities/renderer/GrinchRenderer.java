package io.github.moosyu.entities.renderer;

import io.github.moosyu.entities.mob.Grinch;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public class GrinchRenderer extends HumanoidMobRenderer<Grinch, HumanoidRenderState, HumanoidModel<HumanoidRenderState>> {
    public GrinchRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.5f);
    }

    @Override
    public @NonNull Identifier getTextureLocation(@NonNull HumanoidRenderState renderState) {
        return UnshatteredUtils.getUnshatteredIdentifier("textures/entity/mobs/grinch.png");
    }

    @Override
    public @NonNull HumanoidRenderState createRenderState() {
        return new HumanoidRenderState();
    }
}
