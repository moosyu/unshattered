package io.github.moosyu.gui.screens;

import io.github.moosyu.gui.menus.ReforgeAnvilMenu;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.NonNull;

public class ReforgeAnvilScreen extends AbstractContainerScreen<ReforgeAnvilMenu> {
    private final Player player;

    public ReforgeAnvilScreen(ReforgeAnvilMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.player = inventory.player;
        this.titleLabelX = 60;
    }

    @Override
    public void extractBackground(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, UnshatteredUtils.getUnshatteredIdentifier("textures/gui/anvil.png"), this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
    }
}
