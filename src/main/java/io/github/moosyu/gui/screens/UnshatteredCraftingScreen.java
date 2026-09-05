package io.github.moosyu.gui.screens;

import io.github.moosyu.gui.menus.UnshatteredCraftingMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class UnshatteredCraftingScreen extends AbstractContainerScreen<UnshatteredCraftingMenu> {
    public UnshatteredCraftingScreen(UnshatteredCraftingMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }
}
