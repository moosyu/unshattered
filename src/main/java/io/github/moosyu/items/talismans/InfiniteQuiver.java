package io.github.moosyu.items.talismans;

import io.github.moosyu.abilities.AbilityContext;
import io.github.moosyu.abilities.AbilityTriggerResult;
import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.abilities.PassiveAbilityItem;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.UnshatteredRarity;
import io.github.moosyu.util.UnshatteredUtils;

import java.util.Optional;
import java.util.Set;

public class InfiniteQuiver extends TalismanItem implements PassiveAbilityItem {
    public InfiniteQuiver(Properties properties) {
        super(properties
        );
    }

    @Override
    public Set<AbilityTriggerType> triggerTypes() {
        return Set.of(AbilityTriggerType.PLAYER_USE_PROJECTILE_WEAPON_AMMO);
    }

    @Override
    public Optional<?> triggerResult() {
        return Optional.of(AbilityTriggerResult.CANCEL_EVENT);
    }
}
