package io.github.moosyu.entities.renderers;

import io.github.moosyu.entities.mobs.SkeletonMiner;
import io.github.moosyu.entities.models.SkeletonMinerModel;
import io.github.moosyu.entities.states.SkeletonMinerRenderState;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.BlockDecorationLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import org.jspecify.annotations.NonNull;

public class SkeletonMinerRenderer extends HumanoidMobRenderer<SkeletonMiner, SkeletonMinerRenderState, SkeletonMinerModel> {
    private static final Identifier TEXTURE = Identifier.withDefaultNamespace("textures/entity/skeleton/skeleton.png");
    private static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();
    private final BlockModelResolver blockModelResolver;

    public SkeletonMinerRenderer(EntityRendererProvider.Context context) {
        super(context, new SkeletonMinerModel(context.bakeLayer(ModelLayers.SKELETON)), 0.5f);
        this.blockModelResolver = context.getBlockModelResolver();

        addLayer(new HumanoidArmorLayer<>(this,
                ArmorModelSet.bake(ModelLayers.SKELETON_ARMOR, context.getModelSet(), SkeletonMinerModel::new),
                context.getEquipmentRenderer())
        );

        addLayer(new BlockDecorationLayer<>(this,
                state -> state.heldTnt,
                this.model::applyHeldTntTransform));
    }

    @Override
    public void extractRenderState(@NonNull SkeletonMiner entity, @NonNull SkeletonMinerRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.holdingTnt = entity.getMainHandItem().is(Items.TNT);

        if (state.holdingTnt) {
            blockModelResolver.update(state.heldTnt, Blocks.TNT.defaultBlockState(), BLOCK_DISPLAY_CONTEXT);
        } else {
            state.heldTnt.clear();
        }

        state.rightHandItemState.clear();
        state.leftHandItemState.clear();
    }

    @Override
    public @NonNull Identifier getTextureLocation(@NonNull SkeletonMinerRenderState state) {
        return TEXTURE;
    }

    @Override
    public @NonNull SkeletonMinerRenderState createRenderState() {
        return new SkeletonMinerRenderState();
    }
}