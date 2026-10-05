package io.github.moosyu.items.weapons.daggers;

import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.ItemType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Weapon;

public class DaggerItem extends Item {
    public DaggerItem(Properties properties) {
        super(properties
                .stacksTo(1)
                .component(DataComponents.WEAPON, new Weapon(1))
        );
    }
}
