package io.github.moosyu.gui.screens;

import io.github.moosyu.gui.menus.StorageMenu;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.jspecify.annotations.NonNull;

public class StorageScreen extends AbstractContainerScreen<StorageMenu> {
    private static final Identifier TEXTURE = UnshatteredUtils.getUnshatteredIdentifier("textures/gui/storage.png");
    public final int IMAGE_WIDTH = 176;
    public final int IMAGE_HEIGHT = 222;
    public final int Y_OFFSET = 0;

    public StorageScreen(StorageMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    public void extractBackground(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);

        int x = (this.width - IMAGE_WIDTH) / 2;
        int y = (this.height - IMAGE_HEIGHT) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y + Y_OFFSET, 0, 125, IMAGE_WIDTH, 98, 256, 256);
    }
}
