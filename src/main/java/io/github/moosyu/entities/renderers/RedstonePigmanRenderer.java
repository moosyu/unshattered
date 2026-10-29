package io.github.moosyu.entities.renderers;

import io.github.moosyu.entities.mobs.RedstonePigman;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.monster.piglin.AdultZombifiedPiglinModel;
import net.minecraft.client.model.monster.piglin.ZombifiedPiglinModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.state.ZombifiedPiglinRenderState;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public class RedstonePigmanRenderer extends HumanoidMobRenderer<RedstonePigman, ZombifiedPiglinRenderState, ZombifiedPiglinModel> {
    public RedstonePigmanRenderer(EntityRendererProvider.Context context) {
        super(context, new AdultZombifiedPiglinModel(context.bakeLayer(ModelLayers.ZOMBIFIED_PIGLIN)), 0.5f);
    }

    @Override
    public void extractRenderState(@NonNull RedstonePigman redstonePigman, @NonNull ZombifiedPiglinRenderState state, float partialTicks) {
        super.extractRenderState(redstonePigman, state, partialTicks);

        state.isAggressive = redstonePigman.isAggressive();
    }

    @Override
    public @NonNull Identifier getTextureLocation(@NonNull ZombifiedPiglinRenderState state) {
        return state.isAggressive ? UnshatteredUtils.getUnshatteredIdentifier("textures/entity/mobs/redstone_pigman_active.png") : UnshatteredUtils.getUnshatteredIdentifier("textures/entity/mobs/redstone_pigman_passive.png");
    }

    @Override
    public @NonNull ZombifiedPiglinRenderState createRenderState() {
        return new ZombifiedPiglinRenderState();
    }
}
