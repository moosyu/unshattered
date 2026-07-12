package io.github.moosyu.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jspecify.annotations.NonNull;

import java.util.function.UnaryOperator;

public class UnshatteredEnchantedItem extends Item {
    public UnshatteredEnchantedItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(@NonNull ItemStack stack) {
        return true;
    }

}
