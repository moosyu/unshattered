package io.github.moosyu.gui.screens;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import io.github.moosyu.gui.menus.SackMenu;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

public class SackScreen extends AbstractContainerScreen<SackMenu> {
    private static final Identifier TEXTURE = UnshatteredUtils.getUnshatteredIdentifier("textures/gui/generic_screen.png");
    private static final Identifier INSERT_SPRITE = UnshatteredUtils.getUnshatteredIdentifier("widgets/insert_button");
    private static final Identifier INSERT_HIGHLIGHTED_SPRITE = UnshatteredUtils.getUnshatteredIdentifier("widgets/insert_button_highlighted");
    private static final Identifier INSERT_DISABLED_SPRITE = UnshatteredUtils.getUnshatteredIdentifier("widgets/insert_button_disabled");
    private static final int IMAGE_WIDTH = 176;
    private static final int TEXTURE_SIZE = 256;
    private static final int MIDDLE_TILE_HEIGHT = 89;

    private final int middleHeight;
    private final Inventory inventory;

    public SackScreen(SackMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, IMAGE_WIDTH, SackMenu.TOP_HEIGHT + SackMenu.getMiddleHeight(menu.sackSlots) + SackMenu.BORDER_HEIGHT + SackMenu.BOTTOM_HEIGHT);
        middleHeight = SackMenu.getMiddleHeight(menu.sackSlots);
        inventoryLabelY = imageHeight - 94;
        this.inventory = inventory;
    }

    @Override
    public void init() {
        super.init();

        for (int i = 0; i < menu.sackSlots; i++) {
            addRenderableWidget(new SackItemWidget(
                    leftPos + 8 + (i % SackMenu.SLOTS_PER_ROW) * 16,
                    topPos + SackMenu.TOP_HEIGHT + (i / SackMenu.SLOTS_PER_ROW) * 16,
                    menu,
                    i));
        }

        addRenderableWidget(new InsertIntoSackWidget(leftPos + IMAGE_WIDTH - 15, topPos + 5, menu));
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
        private final SackMenu menu;
        private final int index;
        private final ItemStack itemStack;
        private int lastCount = -1;

        public SackItemWidget(int x, int y, SackMenu menu, int index) {
            super(x, y, 16, 16, Component.translatable("widget.unshattered.narration.sack_item"));
            this.menu = menu;
            this.index = index;
            itemStack = menu.acceptedItems.get(index).getDefaultInstance();
        }

        @Override
        protected void extractWidgetRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
            int count = menu.counts.get(index);

            if (count != lastCount) {
                lastCount = count;
                setTooltip(Tooltip.create(itemStack.getItemName().copy().withColor(UnshatteredUtils.getItemRarity(itemStack).getColour())
                        .append(Component.literal("\n"))
                        .append(Component.translatable("screen.unshattered.sack.stored").withColor(UnshatteredUtils.GRAY))
                        .append(Component.literal(": ").withColor(UnshatteredUtils.GRAY))
                        .append(Component.literal(String.format("%,d", count) + "/" + String.format("%,d", menu.sackSlotCapacity)).withColor(UnshatteredUtils.GRAY)))
                );
            }

            graphics.item(itemStack, getX(), getY());

            if (isHovered) {
                graphics.requestCursor(CursorTypes.POINTING_HAND);
            }
        }

        @Override
        public void onClick(@NonNull MouseButtonEvent event, boolean doubleClick) {
            if (menu.counts.get(index) > 0) {
                MultiPlayerGameMode gameMode = Minecraft.getInstance().gameMode;
                if (gameMode != null) {
                    gameMode.handleInventoryButtonClick(menu.containerId, index);
                }
            }
        }

        @Override
        protected void updateWidgetNarration(@NonNull NarrationElementOutput output) {
            defaultButtonNarrationText(output);
        }
    }

    public class InsertIntoSackWidget extends AbstractWidget {
        private final SackMenu menu;

        public InsertIntoSackWidget(int x, int y, SackMenu menu) {
            super(x, y, 7, 8, Component.translatable("widget.unshattered.narration.insert_into_sack"));
            this.menu = menu;
        }

        @Override
        protected void extractWidgetRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
            active = false;
            for (int i = 0; i < Inventory.INVENTORY_SIZE; i++) {
                int slot = menu.acceptedItems.indexOf(inventory.getItem(i).getItem());
                if (slot >= 0 && menu.counts.get(slot) < menu.sackSlotCapacity) {
                    active = true;
                    break;
                }
            }

            Identifier sprite = !active ? INSERT_DISABLED_SPRITE : isHovered ? INSERT_HIGHLIGHTED_SPRITE : INSERT_SPRITE;
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, getX(), getY(), getWidth(), getHeight());

            if (isHovered && active) {
                graphics.requestCursor(CursorTypes.POINTING_HAND);
            }
        }

        @Override
        public void onClick(@NonNull MouseButtonEvent event, boolean doubleClick) {
            MultiPlayerGameMode gameMode = Minecraft.getInstance().gameMode;
            if (gameMode != null) {
                gameMode.handleInventoryButtonClick(menu.containerId, menu.sackSlots);
            }
        }

        @Override
        protected void updateWidgetNarration(@NonNull NarrationElementOutput output) {
            defaultButtonNarrationText(output);
        }
    }
}