package io.github.moosyu.items;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;

public class UnshatteredArmorItem extends ArmorItem {
    protected String itemType;

    public UnshatteredArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties, String itemType) {
        super(material, type, properties.stacksTo(1));
        this.itemType = itemType;
    }
}
