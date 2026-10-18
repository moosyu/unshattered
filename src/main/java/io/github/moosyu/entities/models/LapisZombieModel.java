package io.github.moosyu.entities.models;

import io.github.moosyu.entities.mobs.LapisZombie;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import org.jspecify.annotations.NonNull;

public class LapisZombieModel extends HumanoidModel<LapisZombie.LapisZombieRenderState> {
    public LapisZombieModel(ModelPart root) {
        super(root);
    }
}