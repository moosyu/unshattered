package io.github.moosyu.gui.screens;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import io.github.moosyu.data.VendorItem;
import io.github.moosyu.gui.menus.SackMenu;
import io.github.moosyu.packets.AttemptPurchasePacket;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public class SackScreen extends AbstractContainerScreen<SackMenu> {
    private static final Identifier TEXTURE = UnshatteredUtils.getUnshatteredIdentifier("textures/gui/generic_screen.png");
    private static final int IMAGE_WIDTH = 176;
    private static final int TEXTURE_SIZE = 256;
    private static final int MIDDLE_TILE_HEIGHT = 89;
    private final int middleHeight;

    public SackScreen(SackMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, IMAGE_WIDTH, SackMenu.TOP_HEIGHT + SackMenu.getMiddleHeight(menu.sackSlots) + SackMenu.BORDER_HEIGHT + SackMenu.BOTTOM_HEIGHT);
        this.middleHeight = SackMenu.getMiddleHeight(menu.sackSlots);
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void extractBackground(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);

        blitSlice(graphics, 0, 0, SackMenu.TOP_HEIGHT);

        for (int drawn = 0; drawn < middleHeight; drawn += MIDDLE_TILE_HEIGHT) {
            blitSlice(graphics, SackMenu.TOP_HEIGHT + drawn, SackMenu.TOP_HEIGHT,
                    Math.min(MIDDLE_TILE_HEIGHT, middleHeight - drawn));
        }

        blitSlice(graphics, SackMenu.TOP_HEIGHT + middleHeight, SackMenu.TOP_HEIGHT + MIDDLE_TILE_HEIGHT, SackMenu.BORDER_HEIGHT + SackMenu.BOTTOM_HEIGHT);
    }

    private void blitSlice(GuiGraphicsExtractor graphics, int yOffset, int v, int height) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, leftPos, topPos + yOffset, 0, v, IMAGE_WIDTH, height, TEXTURE_SIZE, TEXTURE_SIZE);
    }

    public static class SackItemWidget extends AbstractWidget {
        private final VendorItem vendorItem;
        private final ItemStack itemStack;
        private final Consumer<VendorItem> onRightClick;

        public SackItemWidget(int x, int y, int width, int height, VendorItem vendorItem, Consumer<VendorItem> onRightClick) {
            super(x, y, width, height, Component.translatable("widget.unshattered.narration.sack_item"));

            this.vendorItem = vendorItem;
            this.onRightClick = onRightClick;
            itemStack = vendorItem.item().value().getDefaultInstance();
        }

        @Override
        protected void extractWidgetRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
            graphics.item(itemStack, getX(), getY());

            if (isHovered) {
                graphics.requestCursor(CursorTypes.POINTING_HAND);
            }
        }

        @Override
        protected void updateWidgetNarration(@NonNull NarrationElementOutput narrationElementOutput) {
            defaultButtonNarrationText(narrationElementOutput);
        }

        @Override
        public void onClick(@NonNull MouseButtonEvent event, boolean doubleClick) {
            if (event.button() == UnshatteredUtils.MouseButton.LEFT.getButton()) {
                ClientPacketDistributor.sendToServer(new AttemptPurchasePacket(itemStack, vendorItem.price(), vendorItem.itemTradeRequirements()));
            } else if (vendorItem.sellMultiple()) {
                onRightClick.accept(vendorItem);
            }
        }
    }
}