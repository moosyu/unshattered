package io.github.moosyu.items.accessories;

import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.ItemTypes;
import io.github.moosyu.rarities.UnshatteredRarities;
import net.minecraft.world.item.Item;

// talismans (proper) coming soon i promise
public class BatTalisman extends Item {
    public BatTalisman(Properties properties) {
        super(properties.stacksTo(1)
                .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.ACCESSORY)
                .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.RARE)
                .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
        );
    }
}
