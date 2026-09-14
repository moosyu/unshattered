package io.github.moosyu.gui.screens;

import io.github.moosyu.gui.menus.TalismansMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.NonNull;

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
    public void extractBackground(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);

        int x = (this.width - IMAGE_WIDTH) / 2;
        int y = (this.height - IMAGE_HEIGHT) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y + Y_OFFSET, 0, 126, IMAGE_WIDTH, 96, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y + 1, 0, 0, IMAGE_WIDTH, Y_OFFSET, 256, 256);
    }

    @Override
    public boolean keyPressed(@NonNull KeyEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;

        if (minecraft.options.keyInventory.matches(event) && player != null) {
            player.connection.send(new ServerboundContainerClosePacket(menu.containerId));
            minecraft.setScreen(new UnshatteredInventoryScreen(player.inventoryMenu, player.getInventory(), Component.translatable("container.inventory")));
            return true;
        }

        return super.keyPressed(event);
    }
}