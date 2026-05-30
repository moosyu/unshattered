package io.github.moosyu.screens;

import io.github.moosyu.menus.CraftingMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import static io.github.moosyu.registers.TextureRegister.CRAFTING_SCREEN;

public class CraftingScreen extends AbstractContainerScreen<CraftingMenu> {
    public CraftingScreen(CraftingMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float v, int i, int i1) {
        graphics.blit(CRAFTING_SCREEN, 0, 0, 0, 0, 176, 166, 176, 166);
    }
}
