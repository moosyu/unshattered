package io.github.moosyu.items.tools.drills;

import io.github.moosyu.abilities.AbilityContext;
import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.abilities.PassiveAbilityItem;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.UnshatteredRarity;
import io.github.moosyu.util.UnshatteredUtils;

import java.util.Optional;
import java.util.Set;

public class MithrilDrillSXR226 extends DrillItem implements PassiveAbilityItem {
    public MithrilDrillSXR226(Properties properties) {
        super(properties,
                7,
                64,
                13,
                5,
                "mithril_drill_sx_r226"
        );
    }

    @Override
    public Set<AbilityTriggerType> triggerTypes() {
        return Set.of(AbilityTriggerType.PLAYER_MODIFY_POWDER);
    }

    @Override
    public Optional<Float> triggerResult() {
        return Optional.of(0.2f);
    }
}
