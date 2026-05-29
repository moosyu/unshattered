package io.github.moosyu.items;

import io.github.moosyu.helpers.TooltipHelper;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class NNOArmorItem extends ArmorItem {
    protected String itemType;

    public NNOArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties, String itemType) {
        super(material, type, properties);
        this.itemType = itemType;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        TooltipHelper.displayHoverText(stack, tooltipComponents, itemType);
    }
}
