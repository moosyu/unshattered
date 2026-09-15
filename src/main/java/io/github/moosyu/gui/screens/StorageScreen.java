package io.github.moosyu.gui.screens;

import io.github.moosyu.gui.components.ScrollerWidget;
import io.github.moosyu.gui.menus.StorageMenu;
import io.github.moosyu.packets.UpdateStorageScrollPacket;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.joml.Vector2i;
import org.jspecify.annotations.NonNull;

public class StorageScreen extends AbstractContainerScreen<StorageMenu> {
    private static final Identifier TEXTURE = UnshatteredUtils.getUnshatteredIdentifier("textures/gui/storage.png");
    private final int IMAGE_WIDTH = 176;
    private final int IMAGE_HEIGHT = 222;
    private final int Y_OFFSET = 28;
    public static final int STORAGE_SLOTS_AREA_HEIGHT = 108;
    private ScrollerWidget scroller;
    private Vector2i backgroundTopLeft;

    public StorageScreen(StorageMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();

        this.backgroundTopLeft = new Vector2i((width - IMAGE_WIDTH) / 2, ((height - IMAGE_HEIGHT) / 2) - 28);

        scroller = new ScrollerWidget(backgroundTopLeft.x + 156, backgroundTopLeft.y + 18, 91, _ -> {});
        addRenderableWidget(scroller);
        addRenderableWidget(new EditBox(font, backgroundTopLeft.x + 61, backgroundTopLeft.y + 4, 90, 12, Component.translatable("screen.narration.unshattered.storage.search")));
    }

    @Override
    public void extractBackground(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);


        int x = (width - IMAGE_WIDTH) / 2;
        int y = (height - IMAGE_HEIGHT) / 2;
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

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        ClientPacketDistributor.sendToServer(new UpdateStorageScrollPacket(scrollY < 0));
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }
}
