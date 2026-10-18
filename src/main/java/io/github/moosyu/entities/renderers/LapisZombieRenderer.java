package io.github.moosyu.entities.renderers;

import io.github.moosyu.entities.mobs.LapisZombie;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public class LapisZombieRenderer extends HumanoidMobRenderer<LapisZombie, LapisZombie.LapisZombieRenderState, HumanoidModel<LapisZombie.LapisZombieRenderState>> {
    public LapisZombieRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.ZOMBIE)), 0.5f);

        ArmorModelSet<HumanoidModel<LapisZombie.LapisZombieRenderState>> armour = ArmorModelSet.bake(ModelLayers.ZOMBIE_ARMOR, context.getModelSet(), HumanoidModel::new);
        // there are no baby variants, hopefully this will be fine
        addLayer(new HumanoidArmorLayer<>(this, armour, armour, context.getEquipmentRenderer()));
    }


    @Override
    public @NonNull Identifier getTextureLocation(LapisZombie.@NonNull LapisZombieRenderState renderState) {
        return Identifier.withDefaultNamespace("textures/entity/zombie/zombie.png");
    }

    @Override
    public LapisZombie.@NonNull LapisZombieRenderState createRenderState() {
        return new LapisZombie.LapisZombieRenderState();
    }
}