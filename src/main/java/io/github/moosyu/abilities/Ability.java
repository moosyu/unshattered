package io.github.moosyu.abilities;

import io.github.moosyu.data.components.ItemAbility;
import net.minecraft.resources.Identifier;

import javax.annotation.Nullable;

public class Ability {
    public final ItemAbility data;
    public final @Nullable AbilityTriggerType trigger;
    /**
     * ability results run when the ability is successfully triggered
     */
    public final AbilityResults abilityResults;

    protected Ability(ItemAbility data, @Nullable AbilityTriggerType trigger, AbilityResults abilityResults) {
        this.data = data;
        this.trigger = trigger;
        this.abilityResults = abilityResults;
    }

    protected Ability(ItemAbility data, @Nullable AbilityTriggerType trigger) {
        this(data, trigger, new AbilityResults());
    }

    public final Identifier identifier() {
        return UnshatteredAbilities.REGISTRY.getKey(this);
    }

    public final boolean isTicking() {
        return trigger == AbilityTriggerType.ONGOING || trigger == AbilityTriggerType.TICKED;
    }

    public boolean conditionsMet(AbilityContext context) {
        return true;
    }

    public void onTrigger(AbilityContext context) {}

    public void onFinish(AbilityContext context) {}
}