package io.github.moosyu.abilities.data;

import io.github.moosyu.abilities.*;
import io.github.moosyu.data.components.ItemAbility;
import net.minecraft.tags.DamageTypeTags;

import java.util.Set;

public class Blazeborn extends Ability {
    public Blazeborn() {
        super(new ItemAbility(), AbilityTriggerType.PLAYER_INCOMING_DAMAGE, new AbilityResults().add(AbilityResult.CANCEL_EVENT, true));
    }

    @Override
    public boolean conditionsMet(AbilityContext context) {
        return context.get(AbilityContextKey.DAMAGE_SOURCE).map(source -> source.is(DamageTypeTags.IS_FIRE)).orElse(false);
    }
}
