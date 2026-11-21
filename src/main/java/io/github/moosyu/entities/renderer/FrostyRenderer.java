package io.github.moosyu.entities.renderer;

import io.github.moosyu.entities.mob.Frosty;
import io.github.moosyu.entities.model.FrostyModel;
import io.github.moosyu.entities.states.FrostyRenderState;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public class FrostyRenderer extends LivingEntityRenderer<Frosty, FrostyRenderState, FrostyModel> {
    private static final Identifier TEXTURE =
            UnshatteredUtils.getUnshatteredIdentifier("textures/entity/mobs/frosty.png");

    public FrostyRenderer(EntityRendererProvider.Context context) {
        super(context, new FrostyModel(context.bakeLayer(FrostyModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public @NonNull FrostyRenderState createRenderState() {
        return new FrostyRenderState();
    }

    @Override
    public @NonNull Identifier getTextureLocation(@NonNull FrostyRenderState state) {
        return TEXTURE;
    }
}