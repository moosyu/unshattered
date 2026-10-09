package io.github.moosyu.gui.screens;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import io.github.moosyu.Unshattered;
import io.github.moosyu.data.attachments.PlayerForgeSlotsAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.recipes.ForgeCategoryDisplay;
import io.github.moosyu.data.recipes.ForgeRecipe;
import io.github.moosyu.events.RecipeReceivedHandler;
import io.github.moosyu.gui.menus.ForgeMenu;
import io.github.moosyu.packets.AttemptForgeItemPacket;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import org.jspecify.annotations.NonNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ForgeScreen extends AbstractContainerScreen<ForgeMenu> {
    private static final int CATEGORY_BUTTON_SIZE = 20;
    private static final int SLOT_BUTTON_SIZE = 18;

    private enum Page {
        FORGE(Component.translatable("screen.unshattered.forge")),
        CATEGORIES(Component.translatable("screen.unshattered.forge.title.categories")),
        RECIPES(Component.translatable("screen.unshattered.forge.title.recipes"));

        final Component pageTitle;
        Page(Component pageTitle) {
            this.pageTitle = pageTitle;
        }
    }

    private Page currentPage = Page.FORGE;
    private Integer currentSlotIndex = null;
    private RecipeBookCategory currentCategory = null;

    public ForgeScreen(ForgeMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 204);
        inventoryLabelY = imageHeight - 94;
    }

    @Override
    protected void init() {
        super.init();

        if (currentPage == Page.FORGE) {
            for (int i = 0; i < PlayerForgeSlotsAttachment.MAX_SLOTS; i++) {
                if (currentSlotIndex == null) {
                    int index = i;
                    addRenderableWidget(Button.builder(Component.literal("Slot " + i), _ -> {
                        currentPage = Page.CATEGORIES;
                        currentSlotIndex = index;
                        rebuildWidgets();
                    }).build());
                }
            }
        } else if (currentPage == Page.CATEGORIES) {
            List<RecipeBookCategory> recipeBookCategories = ForgeCategoryDisplay.all();
            for (int i = 0; i < recipeBookCategories.size(); i++) {
                // stupid lambdas...
                int index = i;
                ForgeCategoryWidget widget = new ForgeCategoryWidget(minecraft, recipeBookCategories.get(i), () -> {
                    currentPage = Page.RECIPES;
                    currentCategory = recipeBookCategories.get(index);
                    rebuildWidgets();
                });
                widget.setPosition(leftPos + 44 + ((CATEGORY_BUTTON_SIZE + 14) * (i % 3)), topPos + 25 + ((i / 3) * (CATEGORY_BUTTON_SIZE + 7)));
                addRenderableWidget(widget);
            }

            addBackButton(Page.FORGE);
        } else if (currentPage == Page.RECIPES) {
            List<RecipeHolder<ForgeRecipe>> categoryRecipes = RecipeReceivedHandler.FORGE_RECIPES.stream().filter(recipe ->
                    recipe.value().category() == currentCategory
            ).toList();

            for (int i = 0; i < categoryRecipes.size(); i++) {
                ForgeRecipe value = categoryRecipes.get(i).value();
                ForgeRecipeWidget widget = new ForgeRecipeWidget(minecraft, new ItemStack(value.result().item()), value);
                widget.setPosition(leftPos + 15 + ((i % 8 ) * SLOT_BUTTON_SIZE), topPos + 26 + ((i / 4) * SLOT_BUTTON_SIZE));
                addRenderableWidget(widget);
            }

            addBackButton(Page.CATEGORIES);
        } else {
            Unshattered.LOGGER.error("forge screen init failed");
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
        private static final int ITEM_OFFSET = (CATEGORY_BUTTON_SIZE - ITEM_SIZE) / 2;

        private final Runnable onClick;

        public ForgeCategoryWidget(Minecraft minecraft, RecipeBookCategory category, Runnable onClick) {
            super(minecraft,
                    ITEM_OFFSET,
                    ITEM_OFFSET,
                    CATEGORY_BUTTON_SIZE,
                    CATEGORY_BUTTON_SIZE,
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
                    getX(), getY(), getWidth(), getHeight()
            );

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

    private class ForgeRecipeWidget extends ItemDisplayWidget {
        ItemStack itemStack;
        ForgeRecipe forgeRecipe;

        public ForgeRecipeWidget(Minecraft minecraft, ItemStack itemStack, ForgeRecipe forgeRecipe) {
            super(minecraft, 1, 1, SLOT_BUTTON_SIZE, SLOT_BUTTON_SIZE, Component.translatable("widget.unshattered.narration.forge_recipe"), itemStack, false, true);

            this.itemStack = itemStack;
            this.forgeRecipe = forgeRecipe;
        }

        @Override
        protected void extractWidgetRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, UnshatteredUtils.getUnshatteredIdentifier("textures/gui/sprites/widgets/slot.png"), getX(), getY(), 0, 0, getWidth(), getHeight(), getWidth(), getHeight());

            if (isHovered()) {
                graphics.requestCursor(CursorTypes.POINTING_HAND);
            }

            super.extractWidgetRenderState(graphics, mouseX, mouseY, a);
        }


        @Override
        protected void updateWidgetNarration(@NonNull NarrationElementOutput narrationElementOutput) {
            defaultButtonNarrationText(narrationElementOutput);
        }

        // to disable the annoying outline
        @Override
        public boolean isFocused() {
            return false;
        }

        @Override
        protected void extractTooltip(@NonNull GuiGraphicsExtractor graphics, int x, int y) {
            List<Component> lines = new ArrayList<>(Screen.getTooltipFromItem(minecraft, itemStack));

            lines.add(Component.empty());
            lines.add(Component.translatable("screen.unshattered.forge.items_required").withColor(UnshatteredUtils.YELLOW));
            for (SizedIngredient sizedIngredient : forgeRecipe.ingredients()) {
                Optional<Holder<Item>> ingredientItemHolder = sizedIngredient.ingredient().getValues().stream().findFirst();

                ingredientItemHolder.ifPresent(ingredient -> {
                    Item ingredientItem = ingredient.value();
                    ItemStack ingredientItemStack = new ItemStack(ingredientItem);

                    lines.add(ingredientItemStack.getDisplayName()
                            .copy()
                            .withColor(UnshatteredUtils.getItemRarity(ingredientItemStack).getColour(1.0f))
                            .append(sizedIngredient.count() > 1
                                    ? Component.literal(" x" + sizedIngredient.count()).withColor(UnshatteredUtils.DARK_GRAY)
                                    : Component.empty()
                            )
                    );
                });
            }

            lines.add(Component.empty());
            lines.add(Component.translatable("screen.unshattered.forge.duration")
                    .withColor(UnshatteredUtils.GRAY)
                    .append(Component.literal(" " + getTimeDisplay(forgeRecipe.durationSeconds()))
                            .withColor(UnshatteredUtils.CYAN)
                    )
            );
            lines.add(Component.translatable("screen.unshattered.forge.craft").withColor(UnshatteredUtils.YELLOW));

            graphics.setTooltipForNextFrame(minecraft.font, lines, itemStack.getTooltipImage(), x, y);
        }

        // intellij got really pissed off at me until i made this for whatever reason
        private @NonNull String getTimeDisplay(long seconds) {
            if (seconds < 60) {
                return seconds + "s";
            } else if (seconds < 3600) {
                return (seconds / 60) + "m";
            } else if (seconds < 86400) {
                return (seconds / 3600) + "m";
            } else {
                return (seconds / 86400) + "d";
            }
        }

        @Override
        public void onClick(@NonNull MouseButtonEvent event, boolean doubleClick) {
            ClientPacketDistributor.sendToServer(new AttemptForgeItemPacket(Instant.now().getEpochSecond(),
                    new ItemStack(forgeRecipe.result().item()),
                    currentSlotIndex)
            );

            currentPage = Page.FORGE;
            rebuildWidgets();
        }
    }

    private void addBackButton(Page targetPage) {
        addRenderableWidget(new ImageButton(leftPos + (imageWidth / 2) - (CATEGORY_BUTTON_SIZE / 2),
                topPos + 84,
                CATEGORY_BUTTON_SIZE,
                CATEGORY_BUTTON_SIZE,
                new WidgetSprites(UnshatteredUtils.getUnshatteredIdentifier("widgets/back_button"),
                        UnshatteredUtils.getUnshatteredIdentifier("widgets/back_button_disabled"),
                        UnshatteredUtils.getUnshatteredIdentifier("widgets/back_button_highlighted")
                ),
                _ -> {
                    currentPage = targetPage;
                    rebuildWidgets();
                })
        );
    }
}
