package io.github.moosyu.items.attachments;

import io.github.moosyu.abilities.Ability;
import io.github.moosyu.abilities.AbilityItem;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.function.Supplier;

public class FuelAttachmentItem extends Item implements AbilityItem {
    private final int fuelAmount;
    private final Supplier<? extends Ability> ability;

    public FuelAttachmentItem(Properties properties, int fuelAmount, Supplier<? extends Ability> ability) {
        super(properties.stacksTo(1));

        this.fuelAmount = fuelAmount;
        this.ability = ability;
    }

    public int getFuelAddition() {
        return fuelAmount;
    }

    @Override
    public List<? extends Supplier<? extends Ability>> abilities() {
        return List.of(ability);
    }
}