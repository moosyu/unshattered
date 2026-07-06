package io.github.moosyu.gui.screens;

import com.lowdragmc.lowdraglib2.gui.holder.ModularUIScreen;
import com.lowdragmc.lowdraglib2.gui.texture.SpriteTexture;
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI;
import com.lowdragmc.lowdraglib2.gui.ui.UI;
import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import com.lowdragmc.lowdraglib2.gui.ui.elements.ItemSlot;
import com.lowdragmc.lowdraglib2.gui.ui.elements.Label;
import com.lowdragmc.lowdraglib2.gui.ui.elements.ProgressBar;
import com.lowdragmc.lowdraglib2.gui.ui.elements.ScrollerView;
import com.lowdragmc.lowdraglib2.gui.ui.event.HoverTooltips;
import com.lowdragmc.lowdraglib2.gui.ui.event.UIEvents;
import com.lowdragmc.lowdraglib2.gui.ui.style.StylesheetManager;
import dev.vfyjxf.taffy.style.FlexDirection;
import io.github.moosyu.Unshattered;
import io.github.moosyu.attachments.PlayerSkillsAttachment;
import io.github.moosyu.attachments.UnshatteredAttachments;
import io.github.moosyu.attributes.UnshatteredAttributeTypes;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.util.PlayClientsideSound;
import io.github.moosyu.util.TextUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import org.jspecify.annotations.NonNull;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;

import static io.github.moosyu.Unshattered.MODID;

public class ProfileScreen extends ModularUIScreen {
    public enum Tabs {
        INVENTORY(player -> new ProfileScreen(ProfileScreen.createInventoryScreen(player), Component.translatable("gui.title.unshattered.inventory")), SpriteTexture.of(Identifier.fromNamespaceAndPath(MODID, "textures/gui/icons.png")).setSprite(16, 0, 16, 16)),
        CRAFTING(player -> new ProfileScreen(ProfileScreen.createCraftingScreen(player), Component.translatable("gui.title.unshattered.crafting")), SpriteTexture.of(Identifier.fromNamespaceAndPath(MODID, "textures/gui/icons.png")).setSprite(0, 0, 16, 16)),
        STATS(player -> new ProfileScreen(ProfileScreen.createStatsScreen(player), Component.translatable("gui.title.unshattered.stats")), SpriteTexture.of(Identifier.fromNamespaceAndPath(MODID, "textures/gui/icons.png")).setSprite(32, 0, 16, 16)),
        SKILLS(player -> new ProfileScreen(ProfileScreen.createSkillsScreen(player), Component.translatable("gui.title.unshattered.skills")), SpriteTexture.of(Identifier.fromNamespaceAndPath(MODID, "textures/gui/icons.png")).setSprite(48, 0, 16, 16));

        public final Function<Player, Screen> createScreen;
        private final SpriteTexture spriteTexture;

        Tabs(Function<Player, Screen> createScreen, SpriteTexture spriteTexture) {
            this.createScreen = createScreen;
            this.spriteTexture = spriteTexture;
        }
    }

    private static final Minecraft minecraft = Minecraft.getInstance();
    private static final UnshatteredAttributeValues[] VISIBLE_ATTRIBUTES = Arrays.stream(UnshatteredAttributeValues.values()).filter(value -> value.type != UnshatteredAttributeTypes.INVISIBLE).toArray(UnshatteredAttributeValues[]::new);

    public ProfileScreen(ModularUI modularUI, Component title) {
        super(modularUI, title);
    }

    private static UIElement createTabs(Player player, Tabs currentTab) {
        UIElement tabContainer = new UIElement();

        tabContainer.addClass("tab-container");
        for (Tabs tab : Tabs.values()) {
            boolean isCurrentTab = currentTab == tab;
            tabContainer.addChild(new UIElement()
                    .addClasses("tab", isCurrentTab ? "tab-opened" : "tab-closed")
                    .addEventListener(UIEvents.CLICK, _ -> {
                        if (!isCurrentTab) {
                            minecraft.setScreen(tab.createScreen.apply(player));
                            PlayClientsideSound.playClientsideSound(player, SoundEvents.UI_BUTTON_CLICK.value(), SoundSource.UI, 0.4f);
                        }
                    })
                    .addEventListener(UIEvents.HOVER_TOOLTIPS, event -> {
                        event.hoverTooltips = HoverTooltips.empty().append(Component.translatable("gui.title.unshattered." + tab.name().toLowerCase()));
                    })
                    .addChild(new UIElement()
                            .addClass("tab-icon")
                            .style(style -> style.background(tab.spriteTexture))
                    )
            );
        }

        return tabContainer;
    }

    public static ModularUI createInventoryScreen(Player player) {
        UIElement container = new UIElement();
        UIElement inventoryContainer = new UIElement();
        UIElement armourContainer = new UIElement();
        Inventory inventory = player.getInventory();

        inventoryContainer.addClass("inventory-container");

        // armour
        armourContainer.addClass("armour-slots-container");
        armourContainer.addChildren(
                new ItemSlot(new Slot(inventory, 103, 0, 0)),
                new ItemSlot(new Slot(inventory, 102, 0, 0)),
                new ItemSlot(new Slot(inventory, 101, 0, 0)),
                new ItemSlot(new Slot(inventory, 100, 0, 0))
        );
        inventoryContainer.addChildren(armourContainer, createInventorySlots(player));

        container.addChildren(createTabs(player, Tabs.INVENTORY), inventoryContainer);

        return ModularUI.of(UI.of(container, StylesheetManager.INSTANCE.getStylesheetSafe(Identifier.fromNamespaceAndPath(MODID, "lss/unshattered.lss"))));
    }

    public static ModularUI createCraftingScreen(Player player) {
        UIElement container = new UIElement();
        UIElement craftingScreenContainer = new UIElement();
        UIElement craftingContainer = new UIElement();
        UIElement craftingTableContainer = new UIElement();
        UIElement quickCraftingContainer = new UIElement();

        for (int row = 0; row < 3; row++) {
            UIElement rowSlots = new UIElement().layout(layout -> layout.flexDirection(FlexDirection.ROW));
            for (int slot = 0; slot < 3; slot++) {
                rowSlots.addChild(new ItemSlot());
            }
            craftingTableContainer.addChild(rowSlots);
        }

        for (int row = 0; row < 3; row++) {
            quickCraftingContainer.addChild(new ItemSlot());
        }

        craftingContainer.addChildren(
                craftingTableContainer,
                new ItemSlot().addClass("crafting-results-slot"),
                quickCraftingContainer
        );
        craftingContainer.addClass("crafting-container");
        craftingScreenContainer.addClass("crafting-screen-container");
        craftingScreenContainer.addChildren(craftingContainer, createInventorySlots(player));
        container.addChildren(createTabs(player, Tabs.CRAFTING), craftingScreenContainer);

        return ModularUI.of(UI.of(container, StylesheetManager.INSTANCE.getStylesheetSafe(Identifier.fromNamespaceAndPath(MODID, "lss/unshattered.lss"))));
    }

    public static ModularUI createStatsScreen(Player player) {
        return createScrollableProfileScreen(scrollableContainer -> {

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
                                        (currentAttribute.getValue() - currentAttribute.getBaseValue() > 0 ? " (" + TextUtils.oneDecimalFormat.format(currentAttribute.getValue()) + percentageAddition + ")" : "")
                        ).textStyle(textStyle -> textStyle.textColor(currentAttributeValue.color))
                );
            }
        }, player, Tabs.STATS);
    }

    public static ModularUI createSkillsScreen(Player player) {
        return createScrollableProfileScreen(scrollableContainer -> {
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
        }, player, Tabs.SKILLS);
    }

    private static ModularUI createScrollableProfileScreen(Consumer<ScrollerView> contentBuilder, Player player, Tabs currentTab) {
        UIElement profileContainer = new UIElement();
        ScrollerView scrollableContainer = new ScrollerView();
        UIElement container = new UIElement();

        profileContainer.addChildren(createTabs(player, currentTab), container);
        scrollableContainer.addClass("scrollable-container");
        container.addChild(scrollableContainer);
        container.addClass("base-container");

        contentBuilder.accept(scrollableContainer);

        return ModularUI.of(UI.of(profileContainer, StylesheetManager.INSTANCE.getStylesheetSafe(Identifier.fromNamespaceAndPath(MODID, "lss/unshattered.lss"))));
    }

    private static UIElement createInventorySlots(Player player) {
        UIElement inventoryContainer = new UIElement();
        Inventory inventory = player.getInventory();

        // main inventory
        for (int row = 0; row < 3; row++) {
            UIElement inventorySlotsContainer = new UIElement();
            inventorySlotsContainer.addClass("inventory-slots-container");
            for (int col = 0; col < 9; col++) {
                int slot = 9 + row * 9 + col;
                inventorySlotsContainer.addChild(
                        new ItemSlot(new Slot(inventory, slot, 0, 0))
                );
            }
            inventoryContainer.addChild(inventorySlotsContainer);
        }

        // hotbar
        UIElement hotbarSlotsContainer = new UIElement();
        hotbarSlotsContainer.addClass("hotbar-slots-container");
        for (int slot = 0; slot < 9; slot++) {
            hotbarSlotsContainer.addChild(
                    new ItemSlot(new Slot(inventory, slot, 0, 0))
            );
        }
        inventoryContainer.addChild(hotbarSlotsContainer);
        return inventoryContainer;
    }

    @Override
    public void extractBackground(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        // gets the dim background again, probably remove this when this is an actual thing (assume it's not already and im not just dumb)
        this.extractMenuBackground(graphics);
    }

    // so you can close with e too just like the real inventory
    @Override
    public boolean keyPressed(KeyEvent event) {
        if (event.isEscape() || minecraft.options.keyInventory.getKey().getValue() == event.key()) {
            this.onClose();
            return true;
        }

        return false;
    }
}