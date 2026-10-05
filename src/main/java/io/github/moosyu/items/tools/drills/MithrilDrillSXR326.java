package io.github.moosyu.items.tools.drills;

import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.abilities.PassiveAbilityItem;

import java.util.Optional;
import java.util.Set;

public class MithrilDrillSXR326 extends DrillItem implements PassiveAbilityItem {
    public MithrilDrillSXR326(Properties properties) {
        super(properties, 10, 80, 15, 6, "mithril_drill_sx_r326");
    }

    @Override
    public Set<AbilityTriggerType> triggerTypes() {
        return Set.of(AbilityTriggerType.PLAYER_MODIFY_POWDER);
    }

    @Override
    public Optional<Float> triggerResult() {
        return Optional.of(0.4f);
    }
}