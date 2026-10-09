package io.github.moosyu.gui.screens;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import io.github.moosyu.Unshattered;
import io.github.moosyu.data.recipes.ForgeCategoryDisplay;
import io.github.moosyu.gui.menus.ForgeMenu;
import io.github.moosyu.packets.AttemptPurchasePacket;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.ItemDisplayWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class ForgeScreen extends AbstractContainerScreen<ForgeMenu> {
    private static final int BUTTON_SIZE = 20;

    private enum Page {
        CATEGORIES(Component.translatable("screen.unshattered.forge.title.categories")),
        RECIPES(Component.translatable("screen.unshattered.forge.title.recipes")),
        CRAFTING(Component.translatable("screen.unshattered.forge.title.crafting"));

        final Component pageTitle;
        Page(Component pageTitle) {
            this.pageTitle = pageTitle;
        }
    }

    private Page currentPage = Page.CATEGORIES;

    public ForgeScreen(ForgeMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 204);
        inventoryLabelY = imageHeight - 94;
    }

    @Override
    protected void init() {
        super.init();

        if (currentPage == Page.CATEGORIES) {
            List<RecipeBookCategory> recipeBookCategories = ForgeCategoryDisplay.all();
            for (int i = 0; i < recipeBookCategories.size(); i++) {
                ForgeCategoryWidget widget = new ForgeCategoryWidget(minecraft, recipeBookCategories.get(i), () -> {
                    currentPage = Page.RECIPES;
                    rebuildWidgets();
                });
                widget.setPosition(leftPos + 44 + ((BUTTON_SIZE + 14) * (i % 3)), topPos + 25 + ((i / 3) * (BUTTON_SIZE + 7)));
                addRenderableWidget(widget);
            }
        } else if (currentPage == Page.RECIPES) {
            addRenderableWidget(new BackButtonWidget(leftPos + (imageWidth / 2) - (BUTTON_SIZE / 2), topPos + 84,
                    _ -> {
                        currentPage = Page.CATEGORIES;
                        rebuildWidgets();
                    })
            );
        } else if (currentPage == Page.CRAFTING) {

        } else {
            Unshattered.LOGGER.error("forge screen init failed");
            return;
        }
    }

    @Override
    public void extractBackground(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);

        graphics.blit(RenderPipelines.GUI_TEXTURED, UnshatteredUtils.getUnshatteredIdentifier("textures/gui/forge.png"), leftPos, topPos, 0, 0, 176, 204, 256, 256);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        graphics.text(font, currentPage.pageTitle, titleLabelX, titleLabelY, 0xFF404040, false);
        graphics.text(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, 0xFF404040, false);
    }

    private static class ForgeCategoryWidget extends ItemDisplayWidget {
        private static final int ITEM_SIZE = 16;
        private static final int ITEM_OFFSET = (BUTTON_SIZE - ITEM_SIZE) / 2;

        private final Runnable onClick;

        public ForgeCategoryWidget(Minecraft minecraft, RecipeBookCategory category, Runnable onClick) {
            super(minecraft,
                    ITEM_OFFSET,
                    ITEM_OFFSET,
                    BUTTON_SIZE,
                    BUTTON_SIZE,
                    Component.translatable("widget.unshattered.narration.forge_category"),
                    ForgeCategoryDisplay.getIcon(category),
                    false,
                    false
            );

            setTooltip(Tooltip.create(ForgeCategoryDisplay.getName(category).copy()
                    .append(Component.literal("\n"))
                    .append(ForgeCategoryDisplay.getDescription(category))
                    .append(Component.literal("\n"))
                    .append(Component.translatable("screen.unshattered.forge.title.browse").withColor(UnshatteredUtils.YELLOW))
            ));

            this.onClick = onClick;
        }

        @Override
        public void onClick(@NonNull MouseButtonEvent event, boolean doubleClick) {
            onClick.run();
        }

        @Override
        protected void extractWidgetRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED,
                    new WidgetSprites(
                            Identifier.withDefaultNamespace("widget/button"),
                            Identifier.withDefaultNamespace("widget/button_disabled"),
                            Identifier.withDefaultNamespace("widget/button_highlighted")
                    ).get(isActive(), isHovered()),
                    getX(), getY(), getWidth(), getHeight());

            if (isHovered()) {
                graphics.requestCursor(CursorTypes.POINTING_HAND);
            }

            super.extractWidgetRenderState(graphics, mouseX, mouseY, a);
        }

        @Override
        protected void updateWidgetNarration(@NonNull NarrationElementOutput narrationElementOutput) {
            defaultButtonNarrationText(narrationElementOutput);
        }
    }

    private static class ForgeRecipeWidget extends ItemDisplayWidget {
        public ForgeRecipeWidget(Minecraft minecraft, int offsetX, int offsetY, ItemStack itemStack) {
            super(minecraft, offsetX, offsetY, 16, 16, Component.translatable("widget.unshattered.narration.forge_recipe"), itemStack, false, false);
        }


        @Override
        protected void updateWidgetNarration(@NonNull NarrationElementOutput narrationElementOutput) {
            defaultButtonNarrationText(narrationElementOutput);
        }
    }

    private static class BackButtonWidget extends ImageButton {
        public BackButtonWidget(int x, int y, OnPress onPress) {
            super(x, y, BUTTON_SIZE, BUTTON_SIZE, new WidgetSprites(UnshatteredUtils.getUnshatteredIdentifier("widgets/back_button"),
                            UnshatteredUtils.getUnshatteredIdentifier("widgets/back_button_disabled"),
                            UnshatteredUtils.getUnshatteredIdentifier("widgets/back_button_highlighted")
                    ), onPress
            );
        }
    }
}
