package io.github.moosyu.gui.components.tabs;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

@FunctionalInterface
public interface TabIcon {
    void render(GuiGraphicsExtractor graphics, int x, int y);

    static TabIcon item(ItemLike item) {
        // needs to be an array so the lambda thinks its final
        ItemStack[] cache = new ItemStack[1];
        return (graphics, x, y) -> {
            if (cache[0] == null) {
                cache[0] = new ItemStack(item);
            }

            graphics.item(cache[0], x, y);
        };
    }
}