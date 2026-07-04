package io.github.moosyu.gui.screens;

import com.lowdragmc.lowdraglib2.gui.holder.ModularUIScreen;
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI;
import com.lowdragmc.lowdraglib2.gui.ui.UI;
import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import com.lowdragmc.lowdraglib2.gui.ui.elements.Label;
import com.lowdragmc.lowdraglib2.gui.ui.elements.ProgressBar;
import com.lowdragmc.lowdraglib2.gui.ui.elements.ScrollerView;
import com.lowdragmc.lowdraglib2.gui.ui.style.StylesheetManager;
import io.github.moosyu.Unshattered;
import io.github.moosyu.attachments.PlayerSkillsAttachment;
import io.github.moosyu.attachments.UnshatteredAttachments;
import io.github.moosyu.attributes.UnshatteredAttributeTypes;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.util.TextHelpers;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.NonNull;

import java.util.Arrays;
import java.util.function.Consumer;

import static io.github.moosyu.Unshattered.MODID;

public class ProfileScreen extends ModularUIScreen {
    public ProfileScreen(ModularUI modularUI, Component title) {
        super(modularUI, title);
    }

    private static ModularUI createProfileScreen(String title, Consumer<ScrollerView> contentBuilder) {
        UIElement container = new UIElement();
        ScrollerView scrollableContainer = new ScrollerView();

        container.addChild(new Label()
                .setText(title)
                .textStyle(style -> style.fontSize(12.0f))
                .addClass("base-title")
        );
        container.addChild(scrollableContainer);
        container.addClass("base-container");

        contentBuilder.accept(scrollableContainer);
        // ill make this not hardcoded later when i figure out how to...
        scrollableContainer.layout(layout -> layout.height(135));

        return ModularUI.of(UI.of(container, StylesheetManager.INSTANCE.getStylesheetSafe(Identifier.fromNamespaceAndPath(MODID, "lss/unshattered.lss"))));
    }

    public static ModularUI createStatsScreen(Player player) {
        return createProfileScreen("Stats", scrollableContainer -> {
            UnshatteredAttributeValues[] filteredUnshatteredAttributeValues = Arrays.stream(UnshatteredAttributeValues.values()).filter(value -> value.type != UnshatteredAttributeTypes.INVISIBLE).toArray(UnshatteredAttributeValues[]::new);

            for (UnshatteredAttributeValues currentAttributeValue : filteredUnshatteredAttributeValues) {
                AttributeInstance currentAttribute = player.getAttribute(currentAttributeValue.holder);

                if (currentAttribute == null) {
                    Unshattered.LOGGER.error("attribute {} was null! (from stats screen).", currentAttributeValue.id);
                    continue;
                }

                scrollableContainer.addScrollViewChild(new Label().setText(
                                currentAttributeValue.symbol + " " +
                                        Component.translatable("attribute.name.unshattered." + currentAttributeValue.id).getString()
                                        + ": " + TextHelpers.oneDecimalFormat.format(currentAttribute.getBaseValue()) +
                                        (currentAttribute.getValue() - currentAttribute.getBaseValue() > 0 ? " (" + TextHelpers.oneDecimalFormat.format(currentAttribute.getValue()) + ")" : "")
                        ).textStyle(textStyle -> textStyle.textColor(currentAttributeValue.color))
                );
            }
        });
    }

    public static ModularUI createSkillsScreen(Player player) {
        return createProfileScreen("Skills", scrollableContainer -> {
            PlayerSkillsAttachment skills = player.getData(UnshatteredAttachments.PLAYER_SKILLS.get());
            for (PlayerSkillsAttachment.Skill skill : PlayerSkillsAttachment.Skill.values()) {
                scrollableContainer.addScrollViewChildren(
                        new Label().setText(
                                Component.translatable(skill.getTranslationKey()).getString() + " "
                                        + TextHelpers.convertTextToRomanNumeral(skills.getLevel(skills.getExp(skill)))
                        ),
                        new ProgressBar().setProgress(skills.getPercentageToNextLevel(skills.getExp(skill))).label(UIElement::removeSelf)
                );
            }
        });
    }

    @Override
    public void extractBackground(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        // gets the dim background again, probably remove this when this is an actual thing (assume it's not already and im not just dumb)
        this.extractMenuBackground(graphics);
    }
}