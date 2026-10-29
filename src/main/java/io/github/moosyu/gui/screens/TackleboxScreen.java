package io.github.moosyu.gui.screens;

import io.github.moosyu.gui.menus.TackleboxMenu;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jspecify.annotations.NonNull;

public class TackleboxScreen extends AbstractContainerScreen<TackleboxMenu> {
    public TackleboxScreen(TackleboxMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void extractBackground(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);

        graphics.blit(RenderPipelines.GUI_TEXTURED, UnshatteredUtils.getUnshatteredIdentifier("textures/gui/tacklebox.png"), leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);
    }
}