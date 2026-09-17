package io.github.moosyu.gui.screens;

import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.gui.menus.StorageMenu;
import io.github.moosyu.packets.UpdateStoragePagePacket;
import io.github.moosyu.packets.UpdateStorageSearchResultsPacket;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
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
    public static final int STORAGE_SLOTS_AREA_HEIGHT = 108;
    private EditBox searchBox;
    private Button nextButton;
    private Button prevButton;

    public StorageScreen(StorageMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();

        if (minecraft.player == null) return;

        Vector2i backgroundTopLeft = new Vector2i((width - IMAGE_WIDTH) / 2, ((height - IMAGE_HEIGHT) / 2) - 28);

        nextButton = Button.builder(Component.literal("+"), button -> {
            ClientPacketDistributor.sendToServer(new UpdateStoragePagePacket(true));
            menu.updatePage(true);
            updateNavButtonsActiveState();
        }).pos(backgroundTopLeft.x() + 82, backgroundTopLeft.y() + 4).size(12, 12).build();

        prevButton = Button.builder(Component.literal("-"), button -> {
            ClientPacketDistributor.sendToServer(new UpdateStoragePagePacket(false));
            menu.updatePage(false);
            updateNavButtonsActiveState();
        }).pos(backgroundTopLeft.x() + 68, backgroundTopLeft.y() + 4).size(12, 12).build();

        searchBox = new EditBox(font, backgroundTopLeft.x() + 99, backgroundTopLeft.y + 6, 67, 12, Component.translatable("screen.narration.unshattered.storage.search"));
        searchBox.setMaxLength(20);
        searchBox.setBordered(false);
        searchBox.setTextColor(0xFFFFFFFF);
        searchBox.setInvertHighlightedTextColor(false);
        searchBox.setResponder(input -> {
            menu.handleSearch(input);
            ClientPacketDistributor.sendToServer(new UpdateStorageSearchResultsPacket(input));
            updateNavButtonsActiveState();
        });

        addRenderableWidget(nextButton);
        addRenderableWidget(prevButton);
        addRenderableWidget(searchBox);

        updateNavButtonsActiveState();
    }

    /**
     * disables the button if adding one more page would bring them past max or one less would bring it past the minimum
     * (it actually does this in the menu too this is just visual)
     */
    private void updateNavButtonsActiveState() {
        int totalPages = menu.getTotalPages();
        prevButton.active = menu.currentPageIndex > 0;
        nextButton.active = menu.currentPageIndex < totalPages - 1;
    }

    @Override
    public void extractBackground(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);


        int x = (width - IMAGE_WIDTH) / 2;
        int y = (height - IMAGE_HEIGHT) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y - 28, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, 256, 256);
    }

    @Override
    public boolean keyPressed(@NonNull KeyEvent event) {
        if (searchBox.isFocused() && searchBox.keyPressed(event)) {
            return true;
        }

        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;

        if (!searchBox.isFocused() && minecraft.options.keyInventory.matches(event) && player != null) {
            // to skip this.minecraft.setScreen((Screen) null) which resets mouse position
            player.connection.send(new ServerboundContainerClosePacket(menu.containerId));
            minecraft.setScreen(new UnshatteredInventoryScreen(player.inventoryMenu, player.getInventory(), Component.translatable("container.inventory")));
            return true;
        }

        if (searchBox.isFocused()) {
            return true;
        }

        return super.keyPressed(event);
    }

    @Override
    public boolean mouseClicked(@NonNull MouseButtonEvent event, boolean doubleClick) {
        boolean handled = super.mouseClicked(event, doubleClick);

        if (!searchBox.isMouseOver(event.x(), event.y())) {
            searchBox.setFocused(false);
            setFocused(null);
        }

        return handled;
    }

    @Override
    protected void extractLabels(@NonNull GuiGraphicsExtractor graphics, int xm, int ym) {
        if (minecraft.player == null) return;

        graphics.text(font,
                Component.translatable("screen.unshattered.storage.page").append(" (" + (menu.currentPageIndex + 1) + "/"
                        + menu.getTotalPages()
                        + ")"
                ),
                titleLabelX,
                titleLabelY - 56,
                0xFF404040,
                false
        );

        graphics.text(font, playerInventoryTitle.copy(), inventoryLabelX, inventoryLabelY, 0xFF404040, false);
    }
}