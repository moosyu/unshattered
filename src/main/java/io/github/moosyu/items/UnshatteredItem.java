package io.github.moosyu.items;

import io.github.moosyu.registers.DataComponentRegistry;
import net.minecraft.world.item.Item;

public class UnshatteredItem extends Item {
    protected ItemTypes itemType;

    public UnshatteredItem(Properties properties, ItemTypes itemType) {
        super(properties.component(DataComponentRegistry.ITEM_TYPE.get(), itemType));
        this.itemType = itemType;
    }
}
