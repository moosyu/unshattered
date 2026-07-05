package io.github.moosyu.gui.screens;

import com.lowdragmc.lowdraglib2.gui.holder.ModularUIScreen;
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI;
import com.lowdragmc.lowdraglib2.gui.ui.UI;
import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import com.lowdragmc.lowdraglib2.gui.ui.elements.Label;
import com.lowdragmc.lowdraglib2.gui.ui.elements.ProgressBar;
import com.lowdragmc.lowdraglib2.gui.ui.elements.ScrollerView;
import com.lowdragmc.lowdraglib2.gui.ui.event.UIEvents;
import com.lowdragmc.lowdraglib2.gui.ui.style.StylesheetManager;
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
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.NonNull;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;

import static io.github.moosyu.Unshattered.MODID;

public class ProfileScreen extends ModularUIScreen {
    public enum Tabs {
        INVENTORY(player -> new ProfileScreen(ProfileScreen.createInventoryScreen(player), Component.literal("Inventory"))),
        CRAFTING(player -> new ProfileScreen(ProfileScreen.createCraftingScreen(player), Component.literal("Crafting"))),
        STATS(player -> new ProfileScreen(ProfileScreen.createStatsScreen(player), Component.literal("Stats"))),
        SKILLS(player -> new ProfileScreen(ProfileScreen.createSkillsScreen(player), Component.literal("Skills")));

        public final Function<Player, Screen> createScreen;

        Tabs(Function<Player, Screen> createScreen) {
            this.createScreen = createScreen;
        }
    }

    private static final Minecraft minecraft = Minecraft.getInstance();

    public ProfileScreen(ModularUI modularUI, Component title) {
        super(modularUI, title);
    }

    private static UIElement createTabs(Player player) {
        UIElement tabContainer = new UIElement();

        tabContainer.addClass("tab-container");

        for (Tabs tab : Tabs.values()) {
            tabContainer.addChild(new UIElement()
                    .addClasses("tab", "tab-closed")
                    .addEventListener(UIEvents.CLICK, _ -> {
                        minecraft.setScreen(tab.createScreen.apply(player));
                        PlayClientsideSound.playClientsideSound(player, SoundEvents.UI_BUTTON_CLICK.value(), SoundSource.UI, 0.6f);
                    })
            );
        }

        return tabContainer;
    }

    public static ModularUI createInventoryScreen(Player player) {
        UIElement container = new UIElement();
        UIElement inventoryContainer = new UIElement();

        inventoryContainer.addClass("inventory-container");
        container.addChildren(createTabs(player), inventoryContainer);

        return ModularUI.of(UI.of(container, StylesheetManager.INSTANCE.getStylesheetSafe(Identifier.fromNamespaceAndPath(MODID, "lss/unshattered.lss"))));
    }

    public static ModularUI createCraftingScreen(Player player) {
        UIElement container = new UIElement();
        UIElement craftingContainer = new UIElement();

        craftingContainer.addClass("crafting-container");
        container.addChildren(createTabs(player), craftingContainer);

        return ModularUI.of(UI.of(container, StylesheetManager.INSTANCE.getStylesheetSafe(Identifier.fromNamespaceAndPath(MODID, "lss/unshattered.lss"))));
    }

    private static ModularUI createScrollableProfileScreen(String title, Consumer<ScrollerView> contentBuilder, Player player) {
        UIElement profileContainer = new UIElement();
        UIElement container = new UIElement();
        ScrollerView scrollableContainer = new ScrollerView();

        profileContainer.addChildren(createTabs(player), container);
        container.addChild(new Label()
                .setText(title)
                .addClass("base-title")
        );
        container.addChild(scrollableContainer);
        container.addClass("base-container");

        contentBuilder.accept(scrollableContainer);
        // ill make this not hardcoded later when i figure out how to...
        scrollableContainer.layout(layout -> layout.height(135));

        return ModularUI.of(UI.of(profileContainer, StylesheetManager.INSTANCE.getStylesheetSafe(Identifier.fromNamespaceAndPath(MODID, "lss/unshattered.lss"))));
    }

    public static ModularUI createStatsScreen(Player player) {
        return createScrollableProfileScreen("Stats", scrollableContainer -> {
            UnshatteredAttributeValues[] filteredUnshatteredAttributeValues = Arrays.stream(UnshatteredAttributeValues.values()).filter(value -> value.type != UnshatteredAttributeTypes.INVISIBLE).toArray(UnshatteredAttributeValues[]::new);

            for (UnshatteredAttributeValues currentAttributeValue : filteredUnshatteredAttributeValues) {
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
        }, player);
    }

    public static ModularUI createSkillsScreen(Player player) {
        return createScrollableProfileScreen("Skills", scrollableContainer -> {
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
        }, player);
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