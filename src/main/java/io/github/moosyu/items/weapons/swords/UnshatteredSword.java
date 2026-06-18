package io.github.moosyu.items.weapons.swords;

import io.github.moosyu.data.components.DataComponentRegistry;
import io.github.moosyu.items.ItemTypes;
import net.minecraft.world.item.Item;

public class UnshatteredSword extends Item {
    public UnshatteredSword(Properties properties) {
        super(properties.stacksTo(1).component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.SWORD));
    }
}
