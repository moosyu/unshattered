package io.github.moosyu.entities.models;

import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class BaseQuadrupedModel extends QuadrupedModel<LivingEntityRenderState> {
    public BaseQuadrupedModel(ModelPart root) {
        super(root);
    }
}
