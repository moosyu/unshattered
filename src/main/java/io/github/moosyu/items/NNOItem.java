package io.github.moosyu.items;

import io.github.moosyu.helpers.TooltipHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class NNOItem extends Item {
    protected String itemType;

    public NNOItem(Properties properties, String itemType) {
        super(properties);
        this.itemType = itemType;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        TooltipHelper.displayHoverText(stack, tooltipComponents, itemType);
    }
}
