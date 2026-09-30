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
        super(properties.component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarity.RARE)
                .component(UnshatteredDataComponents.SELL_VALUE.get(), 10000)
                .component(UnshatteredDataComponents.DESCRIPTION, true)
                .component(UnshatteredDataComponents.ABILITY.get(),
                        new ItemAbility(UnshatteredUtils.getUnshatteredIdentifier("bottomless"),
                                0,
                                0,
                                0,
                                true
                        )
                )
        );
    }

    @Override
    public void onAbilityTriggered(AbilityContext context) {}

    @Override
    public void onAbilityFinished(AbilityContext context) {}

    @Override
    public boolean abilityConditionsMet(AbilityContext context) {
        return true;
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
