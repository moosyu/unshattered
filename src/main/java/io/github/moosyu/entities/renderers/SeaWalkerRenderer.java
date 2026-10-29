package io.github.moosyu.entities.renderers;

import io.github.moosyu.entities.mobs.SeaWalker;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.monster.zombie.ZombieModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public class SeaWalkerRenderer extends HumanoidMobRenderer<SeaWalker, ZombieRenderState, ZombieModel<ZombieRenderState>> {
    public SeaWalkerRenderer(EntityRendererProvider.Context context) {
        super(context, new ZombieModel<>(context.bakeLayer(ModelLayers.ZOMBIE)), 0.5f);
    }

    @Override
    public @NonNull Identifier getTextureLocation(@NonNull ZombieRenderState state) {
        return UnshatteredUtils.getUnshatteredIdentifier("textures/entity/mobs/sea_walker.png");
    }

    @Override
    public @NonNull ZombieRenderState createRenderState() {
        return new ZombieRenderState();
    }

    @Override
    public void extractRenderState(@NonNull SeaWalker seaWalker, @NonNull ZombieRenderState state, float partialTick) {
        super.extractRenderState(seaWalker, state, partialTick);
        // so the arms are always out
        state.isAggressive = seaWalker.isAggressive();
    }
}
