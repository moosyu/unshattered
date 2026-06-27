package io.github.moosyu.items.tools.axes;

import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.ItemTypes;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Tool;

import java.util.List;

public class UnshatteredAxeTool extends Item {
    public UnshatteredAxeTool(Properties properties, float miningSpeed) {
        super(properties.stacksTo(1).component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.AXE).component(DataComponents.TOOL, new Tool(List.of(Tool.Rule.minesAndDrops(BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.BLOCK).getOrThrow(BlockTags.MINEABLE_WITH_AXE), miningSpeed)), 1.0f, 0, false)));
    }
}
