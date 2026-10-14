package io.github.moosyu.gui.screens;

import io.github.moosyu.gui.components.tabs.TabBar;
import io.github.moosyu.gui.components.tabs.UnshatteredTabs;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import org.jspecify.annotations.NonNull;

public class UnshatteredInventoryScreen extends AbstractContainerScreen<InventoryMenu> {
    private static final Identifier TEXTURE = Identifier.withDefaultNamespace("textures/gui/container/inventory.png");
    public final int IMAGE_WIDTH = 176;
    public final int IMAGE_HEIGHT = 166;
    private final TabBar tabBar = new TabBar(UnshatteredTabs.ALL, UnshatteredTabs.INVENTORY, TabBar.GENERIC_OFFSET_TOP, TabBar.GENERIC_OFFSET_BOTTOM);
    private float xMouse;
    private float yMouse;

    public UnshatteredInventoryScreen(InventoryMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();

        tabBar.init(leftPos, topPos, this::addRenderableWidget);
    }

    @Override
    public void extractBackground(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        int x = (width - IMAGE_WIDTH) / 2;
        int y = (height - IMAGE_HEIGHT) / 2;

        super.extractBackground(graphics, mouseX, mouseY, a);

        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, 256, 256);

        if (minecraft.player != null) {
            InventoryScreen.extractEntityInInventoryFollowsMouse(graphics, leftPos + 26, topPos + 8, leftPos + 75, topPos + 78, 30, 0.0625F, xMouse, yMouse, minecraft.player);
        }
    }

    @Override
    protected void extractLabels(@NonNull GuiGraphicsExtractor graphics, int xm, int ym) {}

    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);
        this.xMouse = mouseX;
        this.yMouse = mouseY;

        tabBar.displayIcons(graphics);
    }
}