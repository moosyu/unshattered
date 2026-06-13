package io.github.moosyu.entities.renderers;

import io.github.moosyu.entities.GraveyardZombieVillager;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.monster.zombie.ZombieVillagerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.ZombieVillagerRenderState;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public class GraveyardZombieVillagerRenderer extends MobRenderer<GraveyardZombieVillager, ZombieVillagerRenderState, ZombieVillagerModel<ZombieVillagerRenderState>> {
    public GraveyardZombieVillagerRenderer(EntityRendererProvider.Context context) {
        super(context, new ZombieVillagerModel<>(context.bakeLayer(ModelLayers.ZOMBIE_VILLAGER)), 0.5F);
    }

    @Override
    public @NonNull ZombieVillagerRenderState createRenderState() {
        return new ZombieVillagerRenderState();
    }

    @Override
    public @NonNull Identifier getTextureLocation(@NonNull ZombieVillagerRenderState state) {
        return Identifier.fromNamespaceAndPath("minecraft", "textures/entity/zombie_villager/zombie_villager.png");
    }
}