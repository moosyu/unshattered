package io.github.moosyu.entities.renderer;

import io.github.moosyu.entities.decoration.GroundIndicator;
import io.github.moosyu.entities.states.GroundIndicatorRenderState;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jspecify.annotations.NonNull;

public class GroundIndicatorRenderer extends EntityRenderer<GroundIndicator, GroundIndicatorRenderState> {
    public GroundIndicatorRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void extractRenderState(@NonNull GroundIndicator tossedTerrain, @NonNull GroundIndicatorRenderState state, float partialTicks) {
        super.extractRenderState(tossedTerrain, state, partialTicks);

        state.halfWidth = tossedTerrain.getHalfWidth();
        state.halfHeight = tossedTerrain.getHalfHeight();
        state.innerColour = tossedTerrain.getInnerColour();
        state.outlineColour = tossedTerrain.getOutlineColour();
        state.duration = tossedTerrain.getDuration();
    }

    @Override
    public @NonNull GroundIndicatorRenderState createRenderState() {
        return new GroundIndicatorRenderState();
    }
}
