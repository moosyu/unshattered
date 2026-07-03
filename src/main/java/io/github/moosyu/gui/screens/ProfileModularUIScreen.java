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
import io.github.moosyu.util.PlayClientsideSound;
import io.github.moosyu.util.TextHelpers;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.NonNull;

import java.util.Arrays;

import static io.github.moosyu.Unshattered.MODID;

public class ProfileModularUIScreen extends ModularUIScreen {
    private static final int MAIN_BOOK_CONTAINER_WIDTH = 282;
    private static final int MAIN_BOOK_CONTAINER_HEIGHT = 180;
    private static final int ARROW_SIZE = 16;
    private static final int ATTRS_PER_PAGE = 9;
    private static final int PAGES_PER_SPREAD = 2;

    public ProfileModularUIScreen(ModularUI modularUI) {
        super(modularUI, Component.literal("profile screen"));
    }

    public static ModularUI createProfileBook(Player player) {
        if (!player.level().isClientSide()) return null;

        UnshatteredAttributeValues[] attrs = Arrays.stream(UnshatteredAttributeValues.values())
                .filter(a -> a.type != UnshatteredAttributeTypes.INVISIBLE)
                .toArray(UnshatteredAttributeValues[]::new);

        int attrsPerSpread = ATTRS_PER_PAGE * PAGES_PER_SPREAD;
        int totalSpreads = (int) Math.ceil(attrs.length / (double) attrsPerSpread);
        UIElement container = new UIElement();
        UIElement bookContainer = new UIElement();
        UIElement book = new UIElement();
        UIElement bookNavigation = new UIElement();
        UIElement[] arrows = new UIElement[2];
        int[] currentSpread = {0};

        bookContainer.addChildren(book, bookNavigation);
        container.addChildren(bookContainer);
        container.layout(layout -> layout.flexDirection(FlexDirection.ROW));
        bookNavigation.layout(layout -> layout.flexDirection(FlexDirection.ROW));

        book.style(style -> style.background(SpriteTexture.of(Identifier.fromNamespaceAndPath(MODID, "textures/gui/book_gui.png"))))
                .layout(layout -> {
                    layout.width(MAIN_BOOK_CONTAINER_WIDTH);
                    layout.height(MAIN_BOOK_CONTAINER_HEIGHT);
                    layout.flexDirection(FlexDirection.ROW);
                    layout.paddingTop(10);
                    layout.paddingLeft(20);
                    layout.paddingRight(20);
                    layout.paddingBottom(10);
                });

        // initial render
        renderSpread(book, attrs, currentSpread[0], player);

        for (int i = 0; i < 2; i++) {
            final int direction = (i == 0) ? -1 : 1;
            arrows[i] = new UIElement();
            arrows[i].layout(layout -> {
                layout.width(ARROW_SIZE);
                layout.height(ARROW_SIZE);
            });

            if (i == 0) {
                arrows[i].transform(transform -> transform.rotation(180));
                arrows[i].layout(layout -> layout.marginRight(MAIN_BOOK_CONTAINER_WIDTH - (ARROW_SIZE * 2)));
            }

            arrows[i].style(style -> style.background(SpriteTexture.of(Identifier.fromNamespaceAndPath(MODID, "textures/gui/arrow.png"))));
            arrows[i].addEventListener(UIEvents.MOUSE_DOWN, event -> {
                int next = currentSpread[0] + direction;
                if (next < 0 || next >= totalSpreads) return;

                currentSpread[0] = next;
                PlayClientsideSound.playClientsideSound(player, SoundEvents.UI_BUTTON_CLICK.value(), SoundSource.UI, 0.6f);
                renderSpread(book, attrs, currentSpread[0], player);
            });
        }

        bookNavigation.addChildren(arrows[0], arrows[1]);
        return ModularUI.of(UI.of(container));
    }

    private static void renderSpread(UIElement book, UnshatteredAttributeValues[] attrs, int spreadIndex, Player player) {
        book.getChildren().forEach(book::removeChild);

        for (int i = 0; i < PAGES_PER_SPREAD; i++) {
            int pageNumber = spreadIndex * PAGES_PER_SPREAD + i;
            UIElement page = new UIElement();

            page.layout(layout -> {
                layout.width(MAIN_BOOK_CONTAINER_WIDTH - 162);
                layout.height(MAIN_BOOK_CONTAINER_HEIGHT - 22);
            });
            page.style(style -> style.clip(Clip.SCISSOR));

            if (i % 2 == 0) {
                page.layout(layout -> layout.marginRight(14));
            }

            page.addChild(createStatsPage(pageNumber, attrs, player));
            book.addChild(page);
        }
    }

    public static UIElement createStatsPage(int pageNumber, UnshatteredAttributeValues[] attrs, Player player) {
        UIElement statsPage = new UIElement();

        statsPage.addChild(
                new Label().setText("Stats page " + (pageNumber + 1)).textStyle(textStyle -> {
                    textStyle.textAlignHorizontal(Horizontal.CENTER);
                    textStyle.fontSize(12);
                    textStyle.textShadow(false);
                    textStyle.textColor(0xFF000000);
                }).layout(layout -> layout.marginBottom(5))
        );

        for (int i = 0; i < ATTRS_PER_PAGE; i++) {
            int pagedIndex = i + (ATTRS_PER_PAGE * pageNumber);

            if (pagedIndex >= attrs.length) break;

            AttributeInstance playerCurrentAttribute = player.getAttribute(attrs[pagedIndex].holder);
            if (playerCurrentAttribute == null) continue;

            statsPage.addChild(new Label()
                    .setText(Component.translatable("attribute.name.unshattered." + attrs[pagedIndex].id).getString()
                            + ": " + TextHelpers.oneDecimalFormat.format(playerCurrentAttribute.getBaseValue())
                            + (playerCurrentAttribute.getValue() - playerCurrentAttribute.getBaseValue() > 0
                            ? " (" + TextHelpers.oneDecimalFormat.format(playerCurrentAttribute.getValue()) + ")" : "")
                    )
                    .textStyle(textStyle -> {
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
        // gets the dim background again, probably remove this when this is an actual thing (assume it's not already and im not just dumb)
        this.extractMenuBackground(graphics);
    }
}