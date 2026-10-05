package io.github.moosyu.items.talismans;

import io.github.moosyu.abilities.*;
import net.minecraft.world.item.Item;

import java.util.Optional;
import java.util.Set;

public class PiggyBankTalisman extends Item implements PassiveAbilityItem {
    public PiggyBankTalisman(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public Set<AbilityTriggerType> triggerTypes() {
        return Set.of(AbilityTriggerType.PLAYER_TAKE_DAMAGE);
    }

    @Override
    public Optional<AbilityTriggerResult> triggerResult() {
        return Optional.of(AbilityTriggerResult.DISABLE_COIN_LOSS);
    }
}
