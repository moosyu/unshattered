package io.github.moosyu.gui.screens;

import io.github.moosyu.gui.menus.TalismansMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class TalismansScreen extends AbstractContainerScreen<TalismansMenu> {
    private static final Identifier TEXTURE = Identifier.withDefaultNamespace("textures/gui/container/generic_54.png");
    public final int IMAGE_WIDTH = 176;
    public final int IMAGE_HEIGHT = 168;
    public final int Y_OFFSET = 71;

    public TalismansScreen(TalismansMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.inventoryLabelY = IMAGE_HEIGHT - 94;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        int x = (this.width - IMAGE_WIDTH) / 2;
        int y = (this.height - IMAGE_HEIGHT) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0, 0, IMAGE_WIDTH, Y_OFFSET, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y + Y_OFFSET, 0, 126, IMAGE_WIDTH, 96, 256, 256);
    }
}