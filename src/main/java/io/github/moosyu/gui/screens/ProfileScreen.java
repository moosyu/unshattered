package io.github.moosyu.gui.screens;

import com.lowdragmc.lowdraglib2.gui.factory.PlayerUIMenuType;
import com.lowdragmc.lowdraglib2.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib2.gui.texture.SpriteTexture;
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI;
import com.lowdragmc.lowdraglib2.gui.ui.UI;
import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import com.lowdragmc.lowdraglib2.gui.ui.elements.*;
import com.lowdragmc.lowdraglib2.gui.ui.elements.inventory.InventorySlots;
import com.lowdragmc.lowdraglib2.gui.ui.event.HoverTooltips;
import com.lowdragmc.lowdraglib2.gui.ui.event.UIEvents;
import com.lowdragmc.lowdraglib2.gui.ui.style.StylesheetManager;
import com.lowdragmc.lowdraglib2.gui.ui.styletemplate.Sprites;
import dev.vfyjxf.taffy.style.AlignContent;
import dev.vfyjxf.taffy.style.FlexDirection;
import io.github.moosyu.Unshattered;
import io.github.moosyu.attachments.PlayerCollectionsAttachment;
import io.github.moosyu.attachments.PlayerSkillsAttachment;
import io.github.moosyu.attachments.UnshatteredAttachments;
import io.github.moosyu.attributes.UnshatteredAttributeTypes;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.collectables.CollectableCategories;
import io.github.moosyu.collectables.CollectableEntries;
import io.github.moosyu.util.TextUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

import java.util.Arrays;
import java.util.function.Consumer;

import static io.github.moosyu.Unshattered.MODID;

public class ProfileScreen {
    public static final Identifier PROFILE_UI_ID = Identifier.fromNamespaceAndPath(MODID, "profile");

    public enum Tabs {
        INVENTORY("inventory", SpriteTexture.of(Identifier.fromNamespaceAndPath(MODID, "textures/gui/icons.png")).setSprite(16, 0, 16, 16)),
        CRAFTING("crafting", SpriteTexture.of(Identifier.fromNamespaceAndPath(MODID, "textures/gui/icons.png")).setSprite(0, 0, 16, 16)),
        STATS("stats", SpriteTexture.of(Identifier.fromNamespaceAndPath(MODID, "textures/gui/icons.png")).setSprite(32, 0, 16, 16)),
        SKILLS("skills", SpriteTexture.of(Identifier.fromNamespaceAndPath(MODID, "textures/gui/icons.png")).setSprite(48, 0, 16, 16)),
        COLLECTIONS("collections", SpriteTexture.of(Identifier.fromNamespaceAndPath(MODID, "textures/gui/icons.png")).setSprite(0, 16, 16, 16));

        public final String id;
        private final SpriteTexture spriteTexture;

        Tabs(String id, SpriteTexture spriteTexture) {
            this.id = id;
            this.spriteTexture = spriteTexture;
        }
    }

    private static final UnshatteredAttributeValues[] VISIBLE_ATTRIBUTES = Arrays.stream(UnshatteredAttributeValues.values())
            .filter(value -> value.type != UnshatteredAttributeTypes.INVISIBLE)
            .toArray(UnshatteredAttributeValues[]::new);


    public static void openProfile(Player player) {
        PlayerUIMenuType.openUI(player, PROFILE_UI_ID);
    }

    public static ModularUI createProfileScreen(Player player) {
        TabView tabView = new TabView();

        tabView.addTab(createTabHeader(Tabs.INVENTORY), createInventoryContent(player));
        tabView.addTab(createTabHeader(Tabs.CRAFTING), createCraftingContent());
        tabView.addTab(createTabHeader(Tabs.STATS), createScrollableContent(createStatsScroller(player)));
        tabView.addTab(createTabHeader(Tabs.SKILLS), createScrollableContent(createSkillsScroller(player)));
        tabView.addTab(createTabHeader(Tabs.COLLECTIONS), createScrollableContent(createCollectionsScroller(player)));

        return ModularUI.of(UI.of(tabView, StylesheetManager.INSTANCE.getStylesheetSafe(Identifier.fromNamespaceAndPath(MODID, "lss/unshattered.lss"))), player);
    }

    private static Tab createTabHeader(Tabs tab) {
        Tab header = new Tab();
        header.addEventListener(UIEvents.HOVER_TOOLTIPS, event -> event.hoverTooltips = HoverTooltips.empty().append(Component.translatable("gui.title.unshattered." + tab.name().toLowerCase())))
                .addChild(new UIElement().style(style -> style.background(tab.spriteTexture)));
        return header;
    }

    private static UIElement createInventoryContent(Player player) {
        UIElement inventoryContainer = new UIElement();
        UIElement armourContainer = new UIElement();
        Inventory inventory = player.getInventory();

        armourContainer.addClass("armour-slots-container");
        armourContainer.addChildren(
                new ItemSlot(new Slot(inventory, 39, 0, 0)), // helmet
                new ItemSlot(new Slot(inventory, 38, 0, 0)), // chestplate
                new ItemSlot(new Slot(inventory, 37, 0, 0)), // leggings
                new ItemSlot(new Slot(inventory, 36, 0, 0))  // boots
        );

        inventoryContainer.addChildren(armourContainer, new InventorySlots());
        return inventoryContainer;
    }

    private static UIElement createCraftingContent() {
        UIElement craftingScreenContainer = new UIElement();
        UIElement craftingContainer = new UIElement();
        UIElement craftingTableContainer = new UIElement();
        UIElement quickCraftingContainer = new UIElement();
        ProfileCraftingGrid grid = new ProfileCraftingGrid();

        for (int row = 0; row < 3; row++) {
            UIElement rowSlots = new UIElement().layout(layout -> layout.flexDirection(FlexDirection.ROW));
            for (int col = 0; col < 3; col++) {
                int index = row * 3 + col;
                rowSlots.addChild(new ItemSlot().bind(grid.input, index));
            }
            craftingTableContainer.addChild(rowSlots);
        }

        for (int slot = 0; slot < 3; slot++) {
            // slot + 1 because 0 is reserved for the results
            quickCraftingContainer.addChild(new ItemSlot().bind(grid.result, slot + 1));
        }

        craftingContainer.addChildren(
                craftingTableContainer,
                new UIElement().style(style -> style.background(SpriteTexture.of(Identifier.fromNamespaceAndPath(MODID, "textures/gui/unshattered_base_gui.png"))
                        .setSprite(0, 32, 25, 15)))
                        .layout(layout -> {
                            layout.width(25);
                            layout.height(15);
                        }),
                new ItemSlot().bind(grid.result, 0),
                quickCraftingContainer
        );
        craftingContainer.addClass("crafting-container");
        craftingScreenContainer.addChildren(craftingContainer, new InventorySlots()).layout(layout -> layout.justifyContent(AlignContent.CENTER));

        return craftingScreenContainer;
    }

    public static class ProfileCraftingGrid {
        public final ItemStacksResourceHandler input;
        public final ItemStacksResourceHandler result = new ItemStacksResourceHandler(4);

        public ProfileCraftingGrid() {
            this.input = new ItemStacksResourceHandler(9);
        }
    }

    private static UIElement createScrollableContent(Consumer<ScrollerView> contentBuilder) {
        ScrollerView scrollableContainer = new ScrollerView();

        scrollableContainer.addClass("scrollable-container");
        contentBuilder.accept(scrollableContainer);

        return scrollableContainer;
    }

    private static Consumer<ScrollerView> createStatsScroller(Player player) {
        return scrollableContainer -> {
            for (UnshatteredAttributeValues currentAttributeValue : VISIBLE_ATTRIBUTES) {
                AttributeInstance currentAttribute = player.getAttribute(currentAttributeValue.holder);
                String percentageAddition = currentAttributeValue.percentage ? "%" : "";
                if (currentAttribute == null) {
                    Unshattered.LOGGER.error("attribute {} was null! (from stats screen).", currentAttributeValue.id);
                    continue;
                }

                scrollableContainer.addScrollViewChild(new Label().setText(
                        currentAttributeValue.symbol + " " +
                                Component.translatable("attribute.name.unshattered." + currentAttributeValue.id).getString()
                                + ": " + TextUtils.oneDecimalFormat.format(currentAttribute.getBaseValue()) + percentageAddition +
                                (currentAttribute.getValue() - currentAttribute.getBaseValue() > 0
                                        ? " (" + TextUtils.oneDecimalFormat.format(currentAttribute.getValue()) + percentageAddition + ")"
                                        : "")
                        ).textStyle(textStyle -> textStyle.textColor(currentAttributeValue.color))
                );
            }
        };
    }

    private static Consumer<ScrollerView> createSkillsScroller(Player player) {
        return scrollableContainer -> {
            PlayerSkillsAttachment skills = player.getData(UnshatteredAttachments.PLAYER_SKILLS.get());

            for (PlayerSkillsAttachment.Skill skill : PlayerSkillsAttachment.Skill.values()) {
                scrollableContainer.addScrollViewChildren(
                        new Label().setText(
                                Component.translatable(skill.getTranslationKey()).getString() + " "
                                        + TextUtils.convertTextToRomanNumeral(skills.getLevel(skills.getExp(skill)))
                        ),
                        new ProgressBar().setProgress(skills.getPercentageToNextLevel(skills.getExp(skill))).label(UIElement::removeSelf)
                );
            }
        };
    }

    private static Consumer<ScrollerView> createCollectionsScroller(Player player) {
        return scrollerView -> {
            for (CollectableCategories category : CollectableCategories.values()) {
                scrollerView.addScrollViewChild(new Button().setText(category.name()));
            }
        };
    }
}