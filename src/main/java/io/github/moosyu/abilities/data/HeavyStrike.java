package io.github.moosyu.abilities.data;

import io.github.moosyu.abilities.Ability;
import io.github.moosyu.abilities.AbilityContext;
import io.github.moosyu.abilities.AbilityContextKey;
import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.List;

public class HeavyStrike extends Ability {
    public HeavyStrike() {
        super(new ItemAbility(), AbilityTriggerType.ONGOING);
    }

    @Override
    public List<UnshatteredUtils.AttributeGroup> attributeBonuses(AbilityContext context) {
        return context.get(AbilityContextKey.PLAYER).map(player -> {
            AttributeInstance strength = player.getAttribute(UnshatteredAttributeValues.STRENGTH.holder);
            if (strength == null) {
                return List.<UnshatteredUtils.AttributeGroup>of();
            }

            double bonus = Math.floor(strength.getValue() / 20) * 4;
            return List.of(new UnshatteredUtils.AttributeGroup(
                    UnshatteredAttributeValues.MINING_SPEED.holder,
                    new AttributeModifier(id(), bonus, AttributeModifier.Operation.ADD_VALUE))
            );
        }).orElse(List.of());
    }
}