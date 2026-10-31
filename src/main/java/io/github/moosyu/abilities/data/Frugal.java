package io.github.moosyu.abilities.data;

import io.github.moosyu.abilities.*;
import io.github.moosyu.data.components.ItemAbility;

import java.util.Set;

public class Frugal extends Ability {
    public Frugal() {
        super(new ItemAbility(), AbilityTriggerType.PLAYER_ATTEMPT_CONSUME_FUEL, new AbilityResults().add(AbilityResult.CANCEL_EVENT, true));
    }

    @Override
    public boolean conditionsMet(AbilityContext context) {
        return context.get(AbilityContextKey.PLAYER).map(player -> player.getRandom().nextInt(0, 3) == 0).orElse(false);
    }
}
