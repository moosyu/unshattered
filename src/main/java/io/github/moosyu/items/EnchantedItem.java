package io.github.moosyu.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

public class EnchantedItem extends Item {
    public EnchantedItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(@NonNull ItemStack stack) {
        return true;
    }

}
