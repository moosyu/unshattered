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
import io.github.moosyu.data.dialogue.DialogueChoice;
import io.github.moosyu.data.dialogue.DialogueNode;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import static io.github.moosyu.Unshattered.MODID;

public class DialogueScreen extends ModularUIScreen {
    public DialogueScreen(Component talkableName, DialogueNode initialDialogue) {
        super(createDialogueBox(talkableName, initialDialogue), Component.literal("Dialogue"));
    }

    private static ModularUI createDialogueBox(Component talkableName, DialogueNode initialDialogue) {
        UIElement root = new UIElement().layout(layout -> layout
                .widthPercent(100)
                .heightPercent(100)
        );

        UIElement dialogueContainer = new UIElement().layout(layout -> layout
                .widthPercent(100)
                .heightPercent(100)
                .paddingBottom(30)
                .flexDirection(FlexDirection.COLUMN)
                .justifyContent(AlignContent.FLEX_END)
                .alignItems(AlignItems.CENTER)
        );

        ScrollerView dialogueBox = new ScrollerView();

        dialogueBox.layout(layout -> layout
                .widthPercent(70)
                .heightPercent(25)
                .marginBottom(5)
        );

        UIElement dialogueChoiceContainer = new UIElement();

        dialogueBox.style(style -> style.background(
                SpriteTexture.of(Identifier.fromNamespaceAndPath(MODID, "textures/gui/unshattered_base_gui.png"))
                        .setSprite(50, 11, 4, 4).setBorder(1)
        ));

        dialogueBox.addScrollViewChild(new Label().setText(initialDialogue.text()).textStyle(textStyle -> textStyle.textWrap(TextWrap.WRAP)));
        if (initialDialogue.dialogueChoices().size() > 1) {
            for (DialogueChoice dialogueChoice : initialDialogue.dialogueChoices()) {
                dialogueChoiceContainer.addChild(new Button().setText(dialogueChoice.text()));
            }
        } else {
            if (initialDialogue.dialogueChoices().isEmpty()
                    || initialDialogue.dialogueChoices().getFirst().text() == null
                    || initialDialogue.dialogueChoices().getFirst().text().getString().isEmpty()
            ) {
                dialogueChoiceContainer.addChild(new Button().setText("..."));
            } else {
                dialogueChoiceContainer.addChild(new Button().setText(initialDialogue.dialogueChoices().getFirst().text()));
            }
        }
        dialogueContainer.addChildren(new Label().setText(talkableName).textStyle(textStyle -> textStyle.fontSize(18)).layout(layout -> layout.leftPercent(-34).topPercent(-2)), dialogueBox, dialogueChoiceContainer);
        root.addChild(dialogueContainer);

        UI ui = UI.of(root);
        return ModularUI.of(ui);
    }
}
