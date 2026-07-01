package io.github.moosyu.gui.screens;

import com.lowdragmc.lowdraglib2.gui.holder.ModularUIScreen;
import com.lowdragmc.lowdraglib2.gui.texture.SpriteTexture;
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI;
import com.lowdragmc.lowdraglib2.gui.ui.UI;
import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import com.lowdragmc.lowdraglib2.gui.ui.data.Clip;
import com.lowdragmc.lowdraglib2.gui.ui.data.Horizontal;
import com.lowdragmc.lowdraglib2.gui.ui.elements.Label;
import com.lowdragmc.lowdraglib2.gui.ui.event.UIEvents;
import dev.vfyjxf.taffy.style.FlexDirection;
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

import static io.github.moosyu.Unshattered.MODID;

public class ProfileModularUIScreen extends ModularUIScreen {
    public ProfileModularUIScreen(ModularUI modularUI) {
        super(modularUI, Component.literal("profile screen"));
    }

    public static ModularUI createProfileBook(Player player) {
        if (!player.level().isClientSide()) return null;
        UIElement container = new UIElement();
        UIElement bookContainer = new UIElement();
        UIElement book = new UIElement();
        UIElement bookNavigation = new UIElement();
        UIElement[] pages = new UIElement[2];
        UIElement[] arrows = new UIElement[2];

        bookContainer.addChildren(book, bookNavigation);
        container.addChildren(bookContainer);
        container.layout(layout -> layout.flexDirection(FlexDirection.ROW));
        bookNavigation.layout(layout -> layout.flexDirection(FlexDirection.ROW));

        for (int i = 0; i < pages.length; i++) {
            pages[i] = new UIElement();
            pages[i].layout(layout -> {
                layout.width(120);
                layout.height(158);
            });
            pages[i].style(style -> {
                style.clip(Clip.SCISSOR);
            });
            if (i % 2 == 0) {
                pages[i].layout(layout -> layout.marginRight(14));
            }
            pages[i].addChild(createStatsPage(i, player));
        }

        for (int i = 0; i < 2; i++) {
            arrows[i] = new UIElement();
            arrows[i].layout(layout -> {
                layout.width(16);
                layout.height(16);
            });
            arrows[i].style(style -> style.background(SpriteTexture.of(Identifier.fromNamespaceAndPath(MODID, "textures/gui/arrow.png"))));
            arrows[i].addEventListener(UIEvents.MOUSE_DOWN, event -> {
                System.out.println("hi guys");
            });
        }

        book.addChildren(pages[0], pages[1])
                .style(style -> style.background(SpriteTexture.of(Identifier.fromNamespaceAndPath(MODID, "textures/gui/book_gui.png"))))
                .layout(layout -> {
                    layout.width(282);
                    layout.height(180);
                    layout.flexDirection(FlexDirection.ROW);
                    layout.paddingTop(10);
                    layout.paddingLeft(20);
                    layout.paddingRight(20);
                    layout.paddingBottom(10);
                });
        return ModularUI.of(UI.of(container));
    }

    public static UIElement createStatsPage(int pageNumber, Player player) {
        UIElement statsPage = new UIElement();
        UnshatteredAttributeValues[] unshatteredAttributeValues = Arrays.stream(UnshatteredAttributeValues.values()).filter(attribute -> attribute.type != UnshatteredAttributeTypes.INVISIBLE).toArray(UnshatteredAttributeValues[]::new);
        statsPage.addChild(
                new Label().setText("Stats page " + (pageNumber + 1)).textStyle(textStyle -> {
                            textStyle.textAlignHorizontal(Horizontal.CENTER);
                            textStyle.fontSize(12);
                            textStyle.textShadow(false);
                            textStyle.textColor(0xFF000000);
                        }).layout(layout -> layout.marginBottom(5))
        );
        for (int i = 0; i < 9; i++) {
            int pagedIndex = i + (9 * pageNumber);
            AttributeInstance playerCurrentAttribute = player.getAttribute(unshatteredAttributeValues[pagedIndex].holder);
            if (playerCurrentAttribute == null) continue;
            statsPage.addChild(new Label()
                    .setText(Component.translatable("attribute.name.unshattered." + unshatteredAttributeValues[pagedIndex].id).getString()
                            + ": " + TextHelpers.oneDecimalFormat.format(playerCurrentAttribute.getBaseValue()) + (playerCurrentAttribute.getValue() - playerCurrentAttribute.getBaseValue() > 0 ? " (" + TextHelpers.oneDecimalFormat.format(playerCurrentAttribute.getValue()) + ")" : "")
                    ).textStyle(textStyle -> {
                        textStyle.fontSize(8);
                        textStyle.textShadow(false);
                        textStyle.textColor(0xFF000000);
                    })
            );
        }
        statsPage.layout(layout -> layout.gapColumn(6));
        return statsPage;
    }

    @Override
    public void extractBackground(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        // gets the dim background again
        this.extractMenuBackground(graphics);
    }
}