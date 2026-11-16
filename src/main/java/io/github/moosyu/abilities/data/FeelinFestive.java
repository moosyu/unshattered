package io.github.moosyu.abilities.data;

import io.github.moosyu.abilities.Ability;
import io.github.moosyu.abilities.AbilityResult;
import io.github.moosyu.abilities.AbilityResults;
import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.data.components.ItemAbility;

public class FeelinFestive extends Ability {
    public FeelinFestive(float percentage) {
        super(new ItemAbility(), AbilityTriggerType.PLAYER_FINISH_OPENING_PRESENT, new AbilityResults().add(AbilityResult.ADD_PRESENT_COINS_PERCENT, percentage).add(AbilityResult.ADD_PRESENT_EXPERIENCE_PERCENT, percentage));
    }
}
