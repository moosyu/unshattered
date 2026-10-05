package io.github.moosyu.items.talismans;

import io.github.moosyu.abilities.AbilityTriggerResult;
import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.abilities.PassiveAbilityItem;
import net.minecraft.world.item.Item;

import java.util.Optional;
import java.util.Set;

public class InfiniteQuiver extends Item implements PassiveAbilityItem {
    public InfiniteQuiver(Properties properties) {
        super(properties.stacksTo(1));
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
