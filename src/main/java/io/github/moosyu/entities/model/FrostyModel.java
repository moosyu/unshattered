package io.github.moosyu.entities.model;

import io.github.moosyu.entities.states.FrostyRenderState;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import org.jspecify.annotations.NonNull;

public class FrostyModel extends EntityModel<FrostyRenderState> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(UnshatteredUtils.getUnshatteredIdentifier("frosty"), "main");

	public FrostyModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("head",
				CubeListBuilder.create()
						.texOffs(0, 0).addBox(-4.0f, -9.0f, -4.0f, 8.0f, 8.0f, 8.0f, new CubeDeformation(-0.5f))
						.texOffs(3, 66).addBox(-5.0f, -2.5f, -5.0f, 10.0f, 2.0f, 10.0f, new CubeDeformation(0.0f))
						.texOffs(1, 0).addBox(-3.5f, -0.5f, -5.0f, 1.0f, 5.0f, 2.0f, new CubeDeformation(0.0f))
						.texOffs(1, 0).addBox(-2.5f, -0.5f, -5.0f, 1.0f, 4.0f, 2.0f, new CubeDeformation(0.0f)),
				PartPose.offset(0.0f, 4.0f, 0.0f));

		PartDefinition leftHand = partdefinition.addOrReplaceChild("left_hand",
				CubeListBuilder.create(),
				PartPose.offset(4.5f, 5.25f, 0.0f));

		leftHand.addOrReplaceChild("left_hand_rotation",
				CubeListBuilder.create()
						.texOffs(32, 0).addBox(-0.5f, -1.0f, -1.0f, 12.0f, 2.0f, 2.0f, new CubeDeformation(-0.5f)),
				PartPose.offsetAndRotation(-0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 0.9948f));

		PartDefinition rightHand = partdefinition.addOrReplaceChild("right_hand",
				CubeListBuilder.create(),
				PartPose.offset(-14.5f, 5.25f, 0.0f));

		PartDefinition rightHandFlip = rightHand.addOrReplaceChild("right_hand_flip",
				CubeListBuilder.create(),
				PartPose.offsetAndRotation(5.0f, 1.0f, 0.0f, 0.0f, 3.1416f, 0.0f));

		rightHandFlip.addOrReplaceChild("right_hand_rotation",
				CubeListBuilder.create()
						.texOffs(32, 0).addBox(-0.5f, -1.0f, -1.0f, 12.0f, 2.0f, 2.0f, new CubeDeformation(-0.5f)),
				PartPose.offsetAndRotation(-5.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.9948f));

		partdefinition.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(0, 16).addBox(-5.0f, -10.0f, -5.0f, 10.0f, 10.0f, 10.0f, new CubeDeformation(-0.5f)),
				PartPose.offset(0.0f, 13.0f, 0.0f));

		partdefinition.addOrReplaceChild("body_bottom",
				CubeListBuilder.create()
						.texOffs(0, 36).addBox(-6.0f, -12.0f, -6.0f, 12.0f, 12.0f, 12.0f, new CubeDeformation(-0.5f)),
				PartPose.offset(0.0f, 24.0f, 0.0f));

		return LayerDefinition.create(meshdefinition, 64, 80);
	}

	@Override
	public void setupAnim(@NonNull FrostyRenderState state) {
		super.setupAnim(state);
	}
}