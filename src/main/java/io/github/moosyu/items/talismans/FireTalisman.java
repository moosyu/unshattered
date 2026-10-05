package io.github.moosyu.items.talismans;

import io.github.moosyu.abilities.*;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.UnshatteredRarity;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.tags.DamageTypeTags;

import java.util.Optional;
import java.util.Set;

public class FireTalisman extends TalismanItem implements PassiveAbilityItem {
    public FireTalisman(Properties properties) {
        super(properties);
    }

    @Override
    public boolean abilityConditionsMet(AbilityContext context) {
        return context.get(AbilityContextKey.DAMAGE_SOURCE).map(source -> source.is(DamageTypeTags.IS_FIRE)).orElse(false);
    }

    @Override
    public Set<AbilityTriggerType> triggerTypes() {
        return Set.of(AbilityTriggerType.PLAYER_INCOMING_DAMAGE);
    }

    @Override
    public Optional<?> triggerResult() {
        return Optional.of(AbilityTriggerResult.CANCEL_EVENT);
    }
}
