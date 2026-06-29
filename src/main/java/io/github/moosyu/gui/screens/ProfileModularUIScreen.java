package io.github.moosyu.gui.screens;

import com.lowdragmc.lowdraglib2.gui.holder.ModularUIScreen;
import com.lowdragmc.lowdraglib2.gui.texture.SpriteTexture;
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI;
import com.lowdragmc.lowdraglib2.gui.ui.UI;
import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import com.lowdragmc.lowdraglib2.gui.ui.elements.Label;
import com.lowdragmc.lowdraglib2.gui.ui.event.UIEvents;
import dev.vfyjxf.taffy.style.FlexDirection;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.NonNull;

import static io.github.moosyu.Unshattered.MODID;

public class ProfileModularUIScreen extends ModularUIScreen {
    public ProfileModularUIScreen(ModularUI modularUI) {
        super(modularUI, Component.literal("profile screen"));
    }

    public static ModularUI createBook(Player player) {
        if (!player.level().isClientSide()) return null;
        UIElement root = new UIElement();
        UIElement book = new UIElement();
        UIElement tabContainer = new UIElement();

        root.addChildren(book, tabContainer);
        root.layout(layout -> layout.flexDirection(FlexDirection.ROW));
        book.addChildren(
        ).style(style -> style.background(SpriteTexture.of(Identifier.fromNamespaceAndPath(MODID, "textures/gui/book_gui.png")))
        ).layout(layout -> {
            layout.width(282);
            layout.height(180);
        });

        tabContainer.layout(layout -> {
            layout.marginTop(10.0f);
            layout.marginBottom(10.0f);
        });

        for (int i = 0; i < 4; i++) {
            UIElement tab = new UIElement();
            tabContainer.addChild(tab
                    .layout(layout -> {
                        layout.width(32).height(32);
                        layout.marginBottom(2.0f);
                    })
                    // default state
                    .style(style -> style.background(SpriteTexture.of(Identifier.fromNamespaceAndPath(MODID, "textures/gui/tab_closed.png"))))
                    .addEventListener(UIEvents.MOUSE_ENTER, _ -> tab.style(style -> style.background(SpriteTexture.of(Identifier.fromNamespaceAndPath(MODID, "textures/gui/tab_opened.png")))))
                    .addEventListener(UIEvents.MOUSE_LEAVE, _ -> tab.style(style -> style.background(SpriteTexture.of(Identifier.fromNamespaceAndPath(MODID, "textures/gui/tab_closed.png")))))
                    .addEventListener(UIEvents.CLICK, _ -> player.level().playLocalSound(player.getX(), player.getY(), player.getZ(), SoundEvents.UI_BUTTON_CLICK.value(), SoundSource.UI, 0.4f, 1.0f, false))
            );
        }
        return ModularUI.of(UI.of(root));
    }

    @Override
    public void extractBackground(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        // gets the dim background again
        this.extractMenuBackground(graphics);
    }
}