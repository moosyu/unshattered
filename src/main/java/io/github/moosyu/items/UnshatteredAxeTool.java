package io.github.moosyu.items;

import io.github.moosyu.registers.DataComponentRegistry;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Tier;

public class UnshatteredAxeTool extends AxeItem {
    public UnshatteredAxeTool(Tier tier, Properties properties) {
        super(tier, properties.component(DataComponentRegistry.ITEM_TYPE.get(), ItemTypes.AXE).stacksTo(1));
    }
}
