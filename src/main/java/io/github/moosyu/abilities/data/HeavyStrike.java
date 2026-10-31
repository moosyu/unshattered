package io.github.moosyu.abilities.data;

import io.github.moosyu.abilities.Ability;
import io.github.moosyu.abilities.AbilityContext;
import io.github.moosyu.abilities.AbilityContextKey;
import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.components.ItemAbility;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class HeavyStrike extends Ability {
    public HeavyStrike() {
        super(new ItemAbility(), AbilityTriggerType.TICKED);
    }

    @Override
    public void onTrigger(AbilityContext context) {
        context.get(AbilityContextKey.PLAYER).ifPresent(player -> {
            AttributeInstance strength = player.getAttribute(UnshatteredAttributeValues.STRENGTH.holder);
            AttributeInstance miningSpeed = player.getAttribute(UnshatteredAttributeValues.MINING_SPEED.holder);
            if (miningSpeed != null && strength != null) {
                AttributeModifier attributeModifier = miningSpeed.getModifier(identifier());
                int bonusMiningSpeed = ((int) strength.getValue() / 20) * 4;
                if (attributeModifier != null) {
                    if (attributeModifier.amount() != bonusMiningSpeed) {
                        miningSpeed.removeModifier(identifier());
                        miningSpeed.addTransientModifier(new AttributeModifier(identifier(), bonusMiningSpeed, AttributeModifier.Operation.ADD_VALUE));
                    }
                } else {
                    miningSpeed.addTransientModifier(new AttributeModifier(identifier(), bonusMiningSpeed, AttributeModifier.Operation.ADD_VALUE));
                }
            }
        });
    }

    @Override
    public void onFinish(AbilityContext context) {
        context.get(AbilityContextKey.PLAYER).ifPresent(player -> {
            AttributeInstance strength = player.getAttribute(UnshatteredAttributeValues.STRENGTH.holder);
            AttributeInstance miningSpeed = player.getAttribute(UnshatteredAttributeValues.MINING_SPEED.holder);
            if (miningSpeed != null && strength != null) {
                strength.removeModifier(identifier());
                miningSpeed.removeModifier(identifier());
            }
        });
    }
}
