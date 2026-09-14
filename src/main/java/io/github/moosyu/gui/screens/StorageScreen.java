package io.github.moosyu.gui.screens;

import io.github.moosyu.gui.menus.StorageMenu;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.jspecify.annotations.NonNull;

public class StorageScreen extends AbstractContainerScreen<StorageMenu> {
    private static final Identifier TEXTURE = UnshatteredUtils.getUnshatteredIdentifier("textures/gui/storage.png");
    public final int IMAGE_WIDTH = 176;
    public final int IMAGE_HEIGHT = 222;
    public final int Y_OFFSET = 28;

    public StorageScreen(StorageMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    public void extractBackground(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);

        int x = (this.width - IMAGE_WIDTH) / 2;
        int y = (this.height - IMAGE_HEIGHT) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y - Y_OFFSET, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, 256, 256);
    }

    @Override
    public boolean keyPressed(@NonNull KeyEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;

        if (minecraft.options.keyInventory.matches(event) && player != null) {
            // to skip this.minecraft.setScreen((Screen) null) which resets mouse position
            player.connection.send(new ServerboundContainerClosePacket(menu.containerId));
            minecraft.setScreen(new UnshatteredInventoryScreen(player.inventoryMenu, player.getInventory(), Component.translatable("container.inventory")));
            return true;
        }

        return super.keyPressed(event);
    }

    @Override
    protected void extractLabels(@NonNull GuiGraphicsExtractor graphics, int xm, int ym) {
        graphics.text(this.font, this.title, this.titleLabelX, this.titleLabelY - 56, 0xFF404040, false);
        graphics.text(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 0xFF404040, false);
    }
}
