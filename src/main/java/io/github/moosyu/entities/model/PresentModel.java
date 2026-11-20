package io.github.moosyu.entities.model;

import io.github.moosyu.entities.states.PresentRenderState;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import org.jspecify.annotations.NonNull;

public class PresentModel extends EntityModel<PresentRenderState> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(UnshatteredUtils.getUnshatteredIdentifier("present"), "main");
	private static final float Y_OFFSET = 24.0f;
	private final ModelPart main;

	public PresentModel(ModelPart root) {
		super(root);
		this.main = root.getChild("main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("main",
				CubeListBuilder.create()
						.texOffs(0, 10).addBox(-3.0f, -5.0f, -3.0f, 6.0f, 5.0f, 6.0f, new CubeDeformation(0.0f))
						.texOffs(0, 0).addBox(-4.0f, -7.0f, -4.0f, 8.0f, 2.0f, 8.0f, new CubeDeformation(0.0f)),
				PartPose.offset(0.0f, Y_OFFSET, 0.0f));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(@NonNull PresentRenderState state) {
		super.setupAnim(state);

		main.yRot = state.hRot;
		main.y = Y_OFFSET - state.liftHeight * 16.0f;
	}
}