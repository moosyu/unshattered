package io.github.moosyu.items.attachments;

import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.abilities.PassiveAbilityItem;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.ItemType;
import io.github.moosyu.items.UnshatteredRarity;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.world.item.Item;

import java.util.Optional;
import java.util.Set;

public class FuelAttachmentItem extends Item implements PassiveAbilityItem {
    private final int fuelAmount;
    private final float abilityCooldownDecrease;

    public FuelAttachmentItem(Properties properties, int fuelAmount, float abilityCooldownDecrease) {
        super(properties.stacksTo(1));

        this.fuelAmount = fuelAmount;
        this.abilityCooldownDecrease = abilityCooldownDecrease;
    }

    public int getFuelAddition() {
        return fuelAmount;
    }

    @Override
    public Set<AbilityTriggerType> triggerTypes() {
        return Set.of(AbilityTriggerType.PLAYER_USE_MINING_ABILITY);
    }

    @Override
    public Optional<Float> triggerResult() {
        return Optional.of(abilityCooldownDecrease);
    }
}