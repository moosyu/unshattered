package io.github.moosyu.gui.screens;

import com.lowdragmc.lowdraglib2.gui.holder.ModularUIScreen;
import com.lowdragmc.lowdraglib2.gui.texture.SpriteTexture;
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI;
import com.lowdragmc.lowdraglib2.gui.ui.UI;
import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import com.lowdragmc.lowdraglib2.gui.ui.data.TextWrap;
import com.lowdragmc.lowdraglib2.gui.ui.elements.Button;
import com.lowdragmc.lowdraglib2.gui.ui.elements.Label;
import com.lowdragmc.lowdraglib2.gui.ui.elements.ScrollerView;
import dev.vfyjxf.taffy.style.AlignContent;
import dev.vfyjxf.taffy.style.AlignItems;
import dev.vfyjxf.taffy.style.FlexDirection;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import static io.github.moosyu.Unshattered.MODID;

public class DialogueScreen extends ModularUIScreen {
    public DialogueScreen() {
        super(createDialogueBox(), Component.literal("Dialogue"));
    }

    private static ModularUI createDialogueBox() {
        UIElement root = new UIElement().layout(layout -> layout
                .widthPercent(100)
                .heightPercent(100)
        );

        UIElement dialogueContainer = new UIElement().layout(layout -> layout
                .widthPercent(100)
                .heightPercent(100)
                .flexDirection(FlexDirection.COLUMN)
                .justifyContent(AlignContent.FLEX_END)
                .alignItems(AlignItems.CENTER)
        );

        ScrollerView dialogueBox = new ScrollerView();

        dialogueBox.layout(layout -> layout
                        .widthPercent(70)
                        .heightPercent(25)
        );

        UIElement dialogueChoiceContainer = new UIElement();

        dialogueBox.style(style -> style.background(
                SpriteTexture.of(Identifier.fromNamespaceAndPath(MODID, "textures/gui/unshattered_base_gui.png"))
                        .setSprite(50, 11, 4, 4).setBorder(1)
        ));

        dialogueBox.addScrollViewChild(new Label().setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. In feugiat diam eu semper volutpat. Sed eu diam turpis. Donec dictum bibendum purus, ut ultrices ex bibendum quis. Sed eu justo non elit maximus condimentum nec sed justo. Aenean lacinia nunc at quam euismod, et vehicula augue porta. Duis ac ipsum mollis, condimentum augue id, ullamcorper diam. Nunc eu scelerisque nulla. Nullam a luctus odio. Aenean at libero ut arcu vulputate mattis. Maecenas condimentum volutpat mauris, at bibendum nisl rutrum ut.").textStyle(textStyle -> textStyle.textWrap(TextWrap.WRAP)));
        dialogueChoiceContainer.addChild(new Button());
        dialogueContainer.addChildren(new Label().setText("hi").textStyle(textStyle -> textStyle.fontSize(16)).layout(layout -> layout.leftPercent(-34).topPercent(-2)), dialogueBox, dialogueChoiceContainer);
        root.addChild(dialogueContainer);

        UI ui = UI.of(root);
        return ModularUI.of(ui);
    }
}
