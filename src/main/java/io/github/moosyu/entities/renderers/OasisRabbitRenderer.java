package io.github.moosyu.entities.renderers;

import com.google.common.collect.Maps;
import io.github.moosyu.entities.mobs.OasisRabbit;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.client.model.animal.rabbit.AdultRabbitModel;
import net.minecraft.client.model.animal.rabbit.RabbitModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.RabbitRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.animal.rabbit.Rabbit;
import org.jspecify.annotations.NonNull;

import java.util.Map;

public class OasisRabbitRenderer extends MobRenderer<OasisRabbit, RabbitRenderState, RabbitModel> {
    private static final Map<Rabbit.Variant, Identifier> RABBIT_LOCATIONS = Maps.newEnumMap(Map.of(Rabbit.Variant.BROWN, UnshatteredUtils.getUnshatteredIdentifier("textures/entity/mobs/oasis_rabbit/brown.png"),
            Rabbit.Variant.WHITE_SPLOTCHED, UnshatteredUtils.getUnshatteredIdentifier("textures/entity/mobs/oasis_rabbit/white_splotched.png"),
            Rabbit.Variant.EVIL, UnshatteredUtils.getUnshatteredIdentifier("textures/entity/mobs/oasis_rabbit/caerbannog.png"),
            Rabbit.Variant.WHITE, UnshatteredUtils.getUnshatteredIdentifier("textures/entity/mobs/oasis_rabbit/white.png"),
            Rabbit.Variant.GOLD, UnshatteredUtils.getUnshatteredIdentifier("textures/entity/mobs/oasis_rabbit/gold.png"),
            Rabbit.Variant.BLACK, UnshatteredUtils.getUnshatteredIdentifier("textures/entity/mobs/oasis_rabbit/black.png"),
            Rabbit.Variant.SALT, UnshatteredUtils.getUnshatteredIdentifier("textures/entity/mobs/oasis_rabbit/salt.png"))
    );

    public OasisRabbitRenderer(EntityRendererProvider.Context context) {
        super(context, new AdultRabbitModel(context.bakeLayer(ModelLayers.RABBIT)), 0.3f);
    }

    public @NonNull Identifier getTextureLocation(RabbitRenderState state) {
        return RABBIT_LOCATIONS.get(state.variant);
    }

    public @NonNull RabbitRenderState createRenderState() {
        return new RabbitRenderState();
    }

    public void extractRenderState(@NonNull OasisRabbit oasisRabbit, @NonNull RabbitRenderState state, float partialTicks) {
        super.extractRenderState(oasisRabbit, state, partialTicks);
        state.jumpCompletion = oasisRabbit.getJumpCompletion(partialTicks);
        state.variant = oasisRabbit.getVariant();
        state.hopAnimationState.copyFrom(oasisRabbit.hopAnimationState);
        state.idleHeadTiltAnimationState.copyFrom(oasisRabbit.idleHeadTiltAnimationState);
    }
}