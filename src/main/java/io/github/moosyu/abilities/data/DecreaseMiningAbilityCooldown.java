package io.github.moosyu.abilities.data;

import io.github.moosyu.abilities.Ability;
import io.github.moosyu.abilities.AbilityResult;
import io.github.moosyu.abilities.AbilityResults;
import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.data.components.ItemAbility;

public class DecreaseMiningAbilityCooldown extends Ability {
    public DecreaseMiningAbilityCooldown(float abilityCooldownDecrease) {
        super(new ItemAbility(), AbilityTriggerType.PLAYER_USE_MINING_ABILITY, new AbilityResults().add(AbilityResult.MODIFY_COOLDOWN_AMOUNT, abilityCooldownDecrease));
    }
}
