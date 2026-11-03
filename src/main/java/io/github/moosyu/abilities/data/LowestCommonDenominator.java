package io.github.moosyu.abilities.data;

import io.github.moosyu.abilities.Ability;
import io.github.moosyu.abilities.AbilityResult;
import io.github.moosyu.abilities.AbilityResults;
import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.data.fishing.FishingWeightModifier;

public class LowestCommonDenominator extends Ability {
    public LowestCommonDenominator() {
        super(new ItemAbility(), AbilityTriggerType.PLAYER_FISH, new AbilityResults().add(AbilityResult.MODIFY_FISHING_MOB_WEIGHTS, FishingWeightModifier.boostTopFraction(0.70, 0.25)));
    }
}
