package io.github.moosyu.entities.models;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.moosyu.entities.states.SkeletonMinerRenderState;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.monster.skeleton.SkeletonModel;
import net.minecraft.util.Mth;
import org.jspecify.annotations.NonNull;

public class SkeletonMinerModel extends SkeletonModel<SkeletonMinerRenderState> {
    private static final float HOLD_ANGLE = -1.2f;
    private float armOffset;

    public SkeletonMinerModel(ModelPart root) {
        super(root);
    }

    @Override
    public void setupAnim(@NonNull SkeletonMinerRenderState state) {
        super.setupAnim(state);
        armOffset = 0.0f;
        if (!state.holdingTnt && state.attackTime <= 0.0f) {
            return;
        }

        float hold = HOLD_ANGLE + Mth.sin(state.ageInTicks * 0.1f) * 0.03f;
        float lift = Mth.sin(state.attackTime * Mth.PI) * 0.55f;
        rightArm.xRot = hold - lift;
        leftArm.xRot  = hold - lift;
        body.yRot = 0.0f;
        rightArm.x = -5.0f;
        rightArm.z = 0.0f;
        leftArm.x = 5.0f;
        leftArm.z = 0.0f;
        rightArm.yRot = 0.12f;
        leftArm.yRot  = -0.12f;
        rightArm.zRot = 0.0f;
        leftArm.zRot  = 0.0f;
        armOffset = rightArm.xRot - HOLD_ANGLE;
    }

    public void applyHeldTntTransform(PoseStack poseStack) {
        poseStack.translate(0.0f, 0.13f + armOffset * 0.7f, -0.65f + armOffset * 0.27f);
        poseStack.scale(0.6f, 0.6f, 0.6f);
    }
}