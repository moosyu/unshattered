package io.github.moosyu.gui.screens;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import io.github.moosyu.Unshattered;
import io.github.moosyu.data.attachments.PlayerForgeSlotsAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.recipes.ForgeCategoryDisplay;
import io.github.moosyu.data.recipes.ForgeRecipe;
import io.github.moosyu.events.RecipeReceivedHandler;
import io.github.moosyu.gui.menus.ForgeMenu;
import io.github.moosyu.packets.AttemptClaimForgedItemPacket;
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
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import org.jspecify.annotations.NonNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;

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
    private PlayerForgeSlotsAttachment lastForgeSlots = null;
    private int lastCompletedCount = 0;
    private final Player player;

    public ForgeScreen(ForgeMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 204);
        inventoryLabelY = imageHeight - 94;

        player = Minecraft.getInstance().player;
    }

    @Override
    protected void init() {
        super.init();

        if (currentPage == Page.FORGE) {
            if (player == null) {
                return;
            }

            PlayerForgeSlotsAttachment forgeSlots = player.getData(UnshatteredAttachments.PLAYER_FORGE_SLOTS.get());
            for (int forgeSlotIndex = 0; forgeSlotIndex < PlayerForgeSlotsAttachment.MAX_SLOTS; forgeSlotIndex++) {
                ForgeItemWidget forgeItemWidget = createForgeItemWidget(forgeSlotIndex, forgeSlots);
                addRenderableWidget(forgeItemWidget);
            }

            lastCompletedCount = countCompletedSlots(forgeSlots);
            lastForgeSlots = forgeSlots;
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
    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);

        if (player == null || currentPage != Page.FORGE) {
            return;
        }

        PlayerForgeSlotsAttachment forgeSlots = player.getData(UnshatteredAttachments.PLAYER_FORGE_SLOTS.get());
        for (int i = 0; i < forgeSlots.slots().size(); i++) {
            graphics.blit(RenderPipelines.GUI_TEXTURED,
                    UnshatteredUtils.getUnshatteredIdentifier("textures/gui/sprites/widgets/forge_progress_bar_empty.png"),
                    leftPos + 40 + (18 * i),
                    topPos + 46,
                    0,
                    0,
                    6,
                    51,
                    6,
                    51
            );

            PlayerForgeSlotsAttachment.ForgeSlot slot = forgeSlots.slots().get(i);
            if (slot.itemStack().isPresent()) {
                int filledHeight = (int) (51 * Math.clamp((float) (Instant.now().getEpochSecond() - slot.startTime()) / slot.forgingDuration(), 0.0f, 1.0f));
                graphics.blit(RenderPipelines.GUI_TEXTURED,
                        UnshatteredUtils.getUnshatteredIdentifier("textures/gui/sprites/widgets/forge_progress_bar.png"),
                        leftPos + 40 + (18 * i),
                        topPos + 46,
                        0,
                        0,
                        6,
                        filledHeight,
                        6,
                        51
                );
            }
        }
    }

    private @NonNull ForgeItemWidget createForgeItemWidget(int forgeSlotIndex, PlayerForgeSlotsAttachment forgeSlots) {
        PlayerForgeSlotsAttachment.ForgeSlot forgeSlot = forgeSlots.slots().get(forgeSlotIndex);
        ForgeItemWidget forgeItemWidget = new ForgeItemWidget(minecraft,
                forgeSlot.itemStack().isPresent() ? forgeSlot.itemStack().get() : ItemStack.EMPTY,
                forgeSlotIndex,
                forgeSlot.startTime() + forgeSlot.forgingDuration(),
                () -> {
                    currentPage = Page.CATEGORIES;
                    currentSlotIndex = forgeSlotIndex;
                    rebuildWidgets();
                },
                () -> {
                    if (forgeSlot.itemStack().isPresent()) {
                        ClientPacketDistributor.sendToServer(new AttemptClaimForgedItemPacket(forgeSlot.itemStack().get(), forgeSlotIndex, forgeSlot.startTime() + forgeSlot.forgingDuration()));
                    } else {
                        Unshattered.LOGGER.error("attempted to claim empty item from forge");
                    }
                }
        );
        forgeItemWidget.setPosition(leftPos + 35 + (18 * forgeSlotIndex), topPos + 26);
        return forgeItemWidget;
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

    private class ForgeItemWidget extends ItemDisplayWidget {
        private final boolean emptySlot;
        private final boolean completedSlot;
        private final Runnable onClick;
        private final Runnable onCompleteClick;
        private final Supplier<Component> tooltipSupplier;
        private long lastTooltipSecond = -1;
        private final long endTime;
        private final boolean inProgress;

        public ForgeItemWidget(Minecraft minecraft, ItemStack itemStack, int slotIndex, long endTime, Runnable onClick, Runnable onCompleteClick) {
            Component tooltipText = Component.empty();
            Supplier<Component> supplier = null;
            Player player = minecraft.player;
            boolean tooltipSet = false;
            boolean empty = false;
            boolean completed = false;

            if (player != null) {
                PlayerForgeSlotsAttachment forgeSlots = player.getData(UnshatteredAttachments.PLAYER_FORGE_SLOTS.get());
                PlayerForgeSlotsAttachment.ForgeSlot forgeSlot = forgeSlots.slots().get(slotIndex);

                if (forgeSlot.itemStack().isPresent() && (forgeSlot.startTime() + forgeSlot.forgingDuration()) <= Instant.now().getEpochSecond()) {
                    completed = true;
                    tooltipSet = true;
                    tooltipText = itemStack.getItemName().copy().withColor(UnshatteredUtils.getItemRarity(itemStack).getColour(1.0f))
                            .append(Component.literal("\n"))
                            .append(Component.translatable("screen.unshattered.forge.completed").withColor(UnshatteredUtils.YELLOW));
                } else if (forgeSlots.availableSlots() <= slotIndex) {
                    itemStack = new ItemStack(Items.BEDROCK);
                    tooltipText = Component.translatable("screen.unshattered.forge.locked").withColor(UnshatteredUtils.RED);
                    tooltipSet = true;
                }
            }

            if (!tooltipSet) {
                if (itemStack.isEmpty()) {
                    itemStack = new ItemStack(Items.BLAST_FURNACE);
                    empty = true;
                    tooltipText = Component.translatable("screen.unshattered.forge.slot").withColor(UnshatteredUtils.GREEN)
                            .append(Component.literal(" #" + (slotIndex + 1)).withColor(UnshatteredUtils.GREEN))
                            .append(Component.literal("\n"))
                            .append(Component.translatable("screen.unshattered.forge.view").withColor(UnshatteredUtils.GRAY))
                            .append(Component.literal("\n\n"))
                            .append(Component.translatable("screen.unshattered.forge.start_process").withColor(UnshatteredUtils.YELLOW));
                } else {
                    ItemStack tooltipStack = itemStack;
                    supplier = () -> tooltipStack.getItemName().copy().withColor(UnshatteredUtils.getItemRarity(tooltipStack).getColour(1.0f))
                            .append(Component.literal("\n"))
                            .append(Component.translatable("screen.unshattered.forge.time_remaining").withColor(UnshatteredUtils.GRAY))
                            .append(Component.literal(": ").withColor(UnshatteredUtils.GRAY))
                            .append(Component.literal(getTimeDisplay(Math.max(0, endTime - Instant.now().getEpochSecond()))).withColor(UnshatteredUtils.GREEN))
                            .append("\n")
                            .append(Component.translatable("screen.unshattered.forge.ends").withColor(UnshatteredUtils.CYAN))
                            .append(Component.literal(": ").withColor(UnshatteredUtils.CYAN))
                            .append(Component.literal(LocalDateTime.ofInstant(Instant.ofEpochSecond(endTime), ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("MMMM d, h:mm a", Locale.ENGLISH))).withColor(UnshatteredUtils.CYAN));
                    tooltipText = supplier.get();
                }
            }

            super(minecraft, 0, 0, 16, 16, Component.translatable("widget.unshattered.narration.forge_item"), itemStack, false, false);

            emptySlot = empty;
            completedSlot = completed;
            this.onClick = onClick;
            this.onCompleteClick = onCompleteClick;
            setTooltip(Tooltip.create(tooltipText));
            tooltipSupplier = supplier;
            this.endTime = endTime;
            inProgress = supplier != null;
        }

        @Override
        protected void extractWidgetRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
            if (isHovered()) {
                graphics.requestCursor(CursorTypes.POINTING_HAND);
            }

            if (tooltipSupplier != null) {
                long now = Instant.now().getEpochSecond();
                if (now != lastTooltipSecond) {
                    lastTooltipSecond = now;
                    setTooltip(Tooltip.create(tooltipSupplier.get()));
                }
            }

            super.extractWidgetRenderState(graphics, mouseX, mouseY, a);
        }

        @Override
        public boolean isFocused() {
            return false;
        }

        @Override
        public void onClick(@NonNull MouseButtonEvent event, boolean doubleClick) {
            if (emptySlot) {
                onClick.run();
            } else if (completedSlot || (inProgress && Instant.now().getEpochSecond() >= endTime)) {
                onCompleteClick.run();
            } else {
                UnshatteredUtils.playClientsideSound(player, SoundEvents.VILLAGER_NO, SoundSource.UI, 0.5f);
                return;
            }

            playButtonClickSound(Minecraft.getInstance().getSoundManager());
        }

        @Override
        public void playDownSound(@NonNull SoundManager soundManager) {}
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

                    lines.add(ingredientItemStack.getItemName()
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

        @Override
        public void onClick(@NonNull MouseButtonEvent event, boolean doubleClick) {
            Player player = minecraft.player;

            if (player == null) {
                return;
            }

            if (UnshatteredUtils.canForgeItem(player, forgeRecipe)) {
                ClientPacketDistributor.sendToServer(new AttemptForgeItemPacket(forgeRecipe, currentSlotIndex));

                currentPage = Page.FORGE;
                rebuildWidgets();
            } else {
                player.sendSystemMessage(Component.translatable("screen.unshattered.forge.forge_failed").withColor(UnshatteredUtils.RED));
            }
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

    // intellij got really pissed off at me until i made this for whatever reason
    private static @NonNull String getTimeDisplay(long seconds) {
        if (seconds < 60) {
            return seconds + "s";
        } else if (seconds < 3600) {
            return (seconds / 60) + "m";
        } else if (seconds < 86400) {
            return (seconds / 3600) + "h";
        } else {
            return (seconds / 86400) + "d";
        }
    }

    @Override
    protected void containerTick() {
        super.containerTick();

        if (currentPage == Page.FORGE && lastForgeSlots != null) {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                PlayerForgeSlotsAttachment current = player.getData(UnshatteredAttachments.PLAYER_FORGE_SLOTS.get());
                if (current != lastForgeSlots || countCompletedSlots(current) != lastCompletedCount) {
                    rebuildWidgets();
                }
            }
        }
    }

    private static int countCompletedSlots(PlayerForgeSlotsAttachment forgeSlots) {
        int count = 0;
        for (PlayerForgeSlotsAttachment.ForgeSlot slot : forgeSlots.slots()) {
            if (slot.itemStack().isPresent() && slot.startTime() + slot.forgingDuration() <= Instant.now().getEpochSecond()) {
                count++;
            }
        }
        return count;
    }
}
