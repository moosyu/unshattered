package io.github.moosyu.entities.models;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.BeeRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;

public class HomingBeeModel extends EntityModel<LivingEntityRenderState> {
    protected final ModelPart bone;
    private final ModelPart rightWing;
    private final ModelPart leftWing;
    private final ModelPart frontLeg;
    private final ModelPart midLeg;
    private final ModelPart backLeg;
    private final ModelPart stinger;
    private final ModelPart leftAntenna;
    private final ModelPart rightAntenna;

    public HomingBeeModel(ModelPart root) {
        super(root);

        bone = root.getChild("bone");
        ModelPart body = bone.getChild("body");
        leftAntenna = body.getChild("left_antenna");
        rightAntenna = body.getChild("right_antenna");
        stinger = body.getChild("stinger");
        rightWing = bone.getChild("right_wing");
        leftWing = bone.getChild("left_wing");
        frontLeg = bone.getChild("front_legs");
        midLeg = bone.getChild("middle_legs");
        backLeg = bone.getChild("back_legs");
    }

    protected void bobUpAndDown(float speed, float ageInTicks) {
        bone.xRot = 0.1f + speed * Mth.PI * 0.025f;
        bone.y -= Mth.cos(ageInTicks * 0.18f) * 0.9f;
        frontLeg.xRot = -speed * Mth.PI * 0.1f + (Mth.PI / 8f);
        backLeg.xRot = -speed * Mth.PI * 0.05f + (Mth.PI / 4f);
        leftAntenna.xRot = speed * Mth.PI * 0.03f;
        rightAntenna.xRot = speed * Mth.PI * 0.03f;
    }

    public void setupAnim(BeeRenderState state) {
        super.setupAnim(state);
        stinger.visible = state.hasStinger;
        if (!state.isOnGround) {
            float speed = state.ageInTicks * 120.32113f * (Mth.PI / 180f);
            rightWing.yRot = 0.0f;
            rightWing.zRot = Mth.cos(speed) * Mth.PI * 0.15f;
            leftWing.xRot = rightWing.xRot;
            leftWing.yRot = rightWing.yRot;
            leftWing.zRot = -rightWing.zRot;
            frontLeg.xRot = (Mth.PI / 4f);
            midLeg.xRot = (Mth.PI / 4f);
            backLeg.xRot = (Mth.PI / 4f);
        }

        if (!state.isAngry && !state.isOnGround) {
            float speed = Mth.cos(state.ageInTicks * 0.18f);
            bobUpAndDown(speed, state.ageInTicks);
        }

        float rollAmount = state.rollAmount;
        if (rollAmount > 0.0f) {
            bone.xRot = Mth.rotLerpRad(rollAmount, bone.xRot, 3.0915928f);
        }

    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition bone = root.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0f, 19.0f, 0.0f));
        PartDefinition body = bone.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5f, -4.0f, -5.0f, 7.0f, 7.0f, 10.0f), PartPose.ZERO);
        body.addOrReplaceChild("stinger", CubeListBuilder.create().texOffs(26, 7).addBox(0.0f, -1.0f, 5.0f, 0.0f, 1.0f, 2.0f), PartPose.ZERO);
        body.addOrReplaceChild("left_antenna", CubeListBuilder.create().texOffs(2, 0).addBox(1.5f, -2.0f, -3.0f, 1.0f, 2.0f, 3.0f), PartPose.offset(0.0f, -2.0f, -5.0f));
        body.addOrReplaceChild("right_antenna", CubeListBuilder.create().texOffs(2, 3).addBox(-2.5f, -2.0f, -3.0f, 1.0f, 2.0f, 3.0f), PartPose.offset(0.0f, -2.0f, -5.0f));
        CubeDeformation wingDeformation = new CubeDeformation(0.001f);
        bone.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(0, 18).addBox(-9.0f, 0.0f, 0.0f, 9.0f, 0.0f, 6.0f, wingDeformation), PartPose.offsetAndRotation(-1.5f, -4.0f, -3.0f, 0.0f, -0.2618f, 0.0f));
        bone.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(0, 18).mirror().addBox(0.0f, 0.0f, 0.0f, 9.0f, 0.0f, 6.0f, wingDeformation), PartPose.offsetAndRotation(1.5f, -4.0f, -3.0f, 0.0f, 0.2618f, 0.0f));
        bone.addOrReplaceChild("front_legs", CubeListBuilder.create().addBox("front_legs", -5.0f, 0.0f, 0.0f, 7, 2, 0, 26, 1), PartPose.offset(1.5f, 3.0f, -2.0f));
        bone.addOrReplaceChild("middle_legs", CubeListBuilder.create().addBox("middle_legs", -5.0f, 0.0f, 0.0f, 7, 2, 0, 26, 3), PartPose.offset(1.5f, 3.0f, 0.0f));
        bone.addOrReplaceChild("back_legs", CubeListBuilder.create().addBox("back_legs", -5.0f, 0.0f, 0.0f, 7, 2, 0, 26, 5), PartPose.offset(1.5f, 3.0f, 2.0f));
        return LayerDefinition.create(mesh, 64, 64);
    }
}
