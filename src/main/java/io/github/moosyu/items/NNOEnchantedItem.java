package io.github.moosyu.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class NNOEnchantedItem extends Item {
    public NNOEnchantedItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}
