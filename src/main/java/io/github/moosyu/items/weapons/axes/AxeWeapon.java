package io.github.moosyu.items.weapons.axes;

import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.ItemType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Weapon;

public class AxeWeapon extends Item {
    public AxeWeapon(Properties properties) {
        super(properties.stacksTo(1).component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemType.BATTLE_AXE).component(DataComponents.WEAPON, new Weapon(1))
        );
    }
}