package io.github.moosyu.items;

import io.github.moosyu.data.components.DataComponentRegistry;
import net.minecraft.world.item.Item;

public class UnshatteredAxeWeapon extends Item {
    public UnshatteredAxeWeapon(Properties properties) {
        super(properties.stacksTo(1).component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.BATTLE_AXE));
    }
}