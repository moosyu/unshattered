package io.github.moosyu.items;

import io.github.moosyu.helpers.TooltipHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class UnshatteredItem extends Item {
    protected String itemType;

    public UnshatteredItem(Properties properties, String itemType) {
        super(properties);
        this.itemType = itemType;
    }
}
