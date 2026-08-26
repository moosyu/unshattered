package io.github.moosyu.items.weapons.daggers;

import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.ItemTypes;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Weapon;

public class UnshatteredDagger extends Item {
    public UnshatteredDagger(Properties properties) {
        super(properties
                .stacksTo(1)
                .component(UnshatteredDataComponents.ITEM_TYPE, ItemTypes.DAGGER)
                .component(DataComponents.WEAPON, new Weapon(1))
        );
    }
}
