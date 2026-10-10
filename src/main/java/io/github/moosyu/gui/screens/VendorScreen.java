package io.github.moosyu.gui.screens;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import io.github.moosyu.data.VendorItem;
import io.github.moosyu.gui.menus.VendorMenu;
import io.github.moosyu.packets.AttemptPurchasePacket;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.jspecify.annotations.NonNull;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

public class VendorScreen extends AbstractContainerScreen<VendorMenu> {
    private static final int IMAGE_WIDTH = 176;
    private static final int IMAGE_HEIGHT = 200;
    private static final int BOX_Y = 56;

    private final VendorMenu menu;
    private final Map<VendorItemWidget, List<Component>> widgetTooltips = new HashMap<>();
    @Nullable private VendorItem expandedItem = null;
    private int itemInputAmount = 1;

    public VendorScreen(VendorMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        this.menu = menu;
    }

    @Override
    protected void init() {
        super.init();

        if (expandedItem == null) {
            widgetTooltips.clear();

            int columns = (IMAGE_WIDTH - 14) / 16;
            for (int i = 0; i < menu.getVendorItems().size(); i++) {
                VendorItem vendorItem = menu.getVendorItems().get(i);
                VendorItemWidget vendorItemWidget = new VendorItemWidget(panelX() + (16 * (i % columns)) + 8,
                        (panelY() - 28) + (16 * (i / columns)) + 48,
                        16,
                        16,
                        vendorItem,
                        this::openSellPage
                );

                List<Component> itemTooltip = new ArrayList<>(getTooltipFromItem(minecraft, new ItemStack(vendorItem.item().value())));

                itemTooltip.addAll(buildPriceLines(vendorItem));
                itemTooltip.add(Component.empty());
                itemTooltip.add(Component.translatable("screen.unshattered.vendor.text.left_click").withColor(0xFFFFFF55));

                if (vendorItem.sellMultiple()) {
                    itemTooltip.add(Component.translatable("screen.unshattered.vendor.text.right_click").withColor(0xFFFFFF55));
                }

                widgetTooltips.put(vendorItemWidget, itemTooltip);

                addRenderableWidget(vendorItemWidget);
            }
        } else {
            int editBoxWidth = 40;
            EditBox quantityEntryBox = new EditBox(font, editBoxWidth, 12, Component.translatable("screen.narration.unshattered.vendor.price"));
            quantityEntryBox.setValue(String.valueOf(itemInputAmount));
            quantityEntryBox.setPosition(centerX() - editBoxWidth / 2, panelY() + BOX_Y);
            quantityEntryBox.setFilter(filter -> filter.isEmpty() || filter.matches("^[0-9]+$"));
            quantityEntryBox.setResponder(input -> {
                try {
                    itemInputAmount = Integer.parseInt(input);
                } catch (Exception e) {
                    itemInputAmount = 0;
                }
            });

            int buyButtonWidth = 30;
            SoundlessButton buyButton = new SoundlessButton(centerX() - (buyButtonWidth * 2), panelY() + BOX_Y - 6, buyButtonWidth, _ -> {
                if (itemInputAmount > 0 && expandedItem != null) {
                    ClientPacketDistributor.sendToServer(new AttemptPurchasePacket(new ItemStack(expandedItem.item(), itemInputAmount), expandedItem.price(), expandedItem.itemTradeRequirements()));
                }
            });

            int backButtonWidth = 30;
            Button backButton = Button.builder(Component.literal("Back"), _ -> {
                itemInputAmount = 1;
                expandedItem = null;
                rebuildWidgets();
            }).build();
            backButton.setWidth(buyButtonWidth);
            backButton.setPosition(centerX() + backButtonWidth, panelY() + BOX_Y - 5);

            addRenderableWidget(quantityEntryBox);
            addRenderableWidget(buyButton);
            addRenderableWidget(backButton);
        }
    }

    @Override
    public void extractBackground(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);

        graphics.blit(RenderPipelines.GUI_TEXTURED, UnshatteredUtils.getUnshatteredIdentifier("textures/gui/vendor.png"), (width - IMAGE_WIDTH) / 2, ((height - IMAGE_HEIGHT) / 2) - 28, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, 256, 256);
    }

    @Override
    protected void extractLabels(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        graphics.text(font, title, titleLabelX, titleLabelY - 45, 0xFF404040, false);
        graphics.text(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY - 10, 0xFF404040, false);
    }

    private void openSellPage(VendorItem vendorItem) {
        expandedItem = vendorItem;
        rebuildWidgets();
    }

    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);

        if (expandedItem == null) {
            for (Map.Entry<VendorItemWidget, List<Component>> entry : widgetTooltips.entrySet()) {
                if (entry.getKey().isHovered()) {
                    graphics.setTooltipForNextFrame(font, entry.getValue(), Optional.empty(), mouseX, mouseY);
                    break;
                }
            }
            return;
        }

        int itemX = centerX() - 8;
        int itemY = panelY() + 30;
        ItemStack itemStack = new ItemStack(expandedItem.item(), Math.max(1, itemInputAmount));

        graphics.item(itemStack, itemX, itemY);
        graphics.itemDecorations(font, itemStack, itemX, itemY);

        if (mouseX >= itemX && mouseX < itemX + 16 && mouseY >= itemY && mouseY < itemY + 16) {
            List<Component> tooltipContent = getTooltipFromItem(minecraft, itemStack);
            tooltipContent.addAll(buildPriceLines(expandedItem));
            graphics.setTooltipForNextFrame(font, tooltipContent, itemStack.getTooltipImage(), mouseX, mouseY);
        }
    }

    private int panelX()  {
        return (width - IMAGE_WIDTH) / 2;
    }

    private int panelY()  {
        return (height - IMAGE_HEIGHT) / 2 - 28;
    }

    private int centerX() {
        return panelX() + IMAGE_WIDTH / 2;
    }

    private List<Component> buildPriceLines(VendorItem vendorItem) {
        List<Component> lines = new ArrayList<>();

        lines.add(Component.empty());

        lines.add(Component.translatable("screen.unshattered.vendor.text.cost").withColor(UnshatteredUtils.GRAY));

        vendorItem.price().ifPresent(price ->
                lines.add(Component.literal(String.format("%,d", price) + " ")
                        .append(Component.translatable("screen.unshattered.vendor.text.coins"))
                        .withColor(0xFFF9A604)
                )
        );

        vendorItem.itemTradeRequirements().ifPresent(requirements -> {
            for (ItemStack requirement : requirements) {
                MutableComponent line = requirement.getItemName().copy()
                        .withColor(UnshatteredUtils.getItemRarity(requirement).getColour(1.0f));

                if (requirement.count() > 1) {
                    line.append(Component.literal(" x" + requirement.count())
                            .withColor(UnshatteredUtils.DARK_GRAY));
                }

                lines.add(line);
            }
        });

        return lines;
    }

    public static class VendorItemWidget extends AbstractWidget {
        private final VendorItem vendorItem;
        private final ItemStack itemStack;
        private final Consumer<VendorItem> onRightClick;

        public VendorItemWidget(int x, int y, int width, int height, VendorItem vendorItem, Consumer<VendorItem> onRightClick) {
            super(x, y, width, height, Component.translatable("widget.unshattered.narration.shop_item"));

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
        protected boolean isValidClickButton(MouseButtonInfo buttonInfo) {
            return buttonInfo.button() == UnshatteredUtils.MouseButton.LEFT.getButton() || buttonInfo.button() == UnshatteredUtils.MouseButton.RIGHT.getButton();
        }

        @Override
        public void onClick(@NonNull MouseButtonEvent event, boolean doubleClick) {
            if (event.button() == UnshatteredUtils.MouseButton.LEFT.getButton()) {
                ClientPacketDistributor.sendToServer(new AttemptPurchasePacket(itemStack, vendorItem.price(), vendorItem.itemTradeRequirements()));
            } else if (vendorItem.sellMultiple()) {
                onRightClick.accept(vendorItem);
            }
        }

        @Override
        public void playDownSound(@NonNull SoundManager soundManager) {}
    }

    private static class SoundlessButton extends Button.Plain {
        public SoundlessButton(int x, int y, int width, OnPress onPress) {
            super(x, y, width, 20, Component.translatable("screen.unshattered.vendor.buy"), onPress, Button.DEFAULT_NARRATION);
        }

        @Override
        public void playDownSound(@NonNull SoundManager soundManager) {}
    }
}