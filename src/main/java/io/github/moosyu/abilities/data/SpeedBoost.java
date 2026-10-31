package io.github.moosyu.abilities.data;

import io.github.moosyu.abilities.*;
import io.github.moosyu.data.components.ItemAbility;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public final class SpeedBoost extends ActiveAbility {
    private static final double SPEED_BOOST_AMOUNT = 0.05;

    public SpeedBoost() {
        super(new ItemAbility());
    }

    @Override
    protected boolean canActivate(AbilityContext context) {
        return context.get(AbilityContextKey.PLAYER)
                .map(player -> player.getAttribute(Attributes.MOVEMENT_SPEED) != null)
                .orElse(false);
    }

    @Override
    protected void onActivate(AbilityContext context, boolean refreshing) {
        context.get(AbilityContextKey.PLAYER).ifPresent(player -> {
            AttributeInstance movementSpeedAttribute = player.getAttribute(Attributes.MOVEMENT_SPEED);

            if (movementSpeedAttribute != null) {
                movementSpeedAttribute.addOrUpdateTransientModifier(
                        new AttributeModifier(identifier(),
                                SPEED_BOOST_AMOUNT,
                                AttributeModifier.Operation.ADD_VALUE
                        )
                );
            }
        });
    }

    @Override
    public void onFinish(AbilityContext context) {
        context.get(AbilityContextKey.PLAYER).ifPresent(player -> {
            AttributeInstance movementSpeedAttribute = player.getAttribute(Attributes.MOVEMENT_SPEED);
            if (movementSpeedAttribute != null) {
                movementSpeedAttribute.removeModifier(identifier());
            }
        });
    }
}