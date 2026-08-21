package io.github.moosyu.gui.screens;

import com.lowdragmc.lowdraglib2.gui.holder.ModularUIScreen;
import com.lowdragmc.lowdraglib2.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib2.gui.texture.RectTexture;
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
import io.github.moosyu.data.attachments.PlayerDialogueFlagsAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.dialogue.DialogueChoice;
import io.github.moosyu.data.dialogue.DialogueNode;
import io.github.moosyu.packets.QueueNewFlagsPacket;
import io.github.moosyu.packets.ResetFlagQueuePacket;
import io.github.moosyu.packets.TriggerEventPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import java.util.List;
import java.util.Optional;

import static io.github.moosyu.Unshattered.MODID;

public class DialogueScreen extends ModularUIScreen {
    private static final class UIRefs {
        ScrollerView dialogueBox;
        UIElement dialogueChoiceContainer;
    }

    private final UIRefs refs;

    public DialogueScreen(Component talkableName, DialogueNode initialDialogue, Player player) {
        this(talkableName, new UIRefs());
        showNode(initialDialogue, player);
    }

    private DialogueScreen(Component talkableName, UIRefs refs) {
        super(createDialogueBox(talkableName, refs), Component.literal("Dialogue"));
        this.refs = refs;
    }

    private static ModularUI createDialogueBox(Component talkableName, UIRefs refs) {
        UIElement root = new UIElement().layout(layout -> layout
                .widthPercent(100)
                .heightPercent(100)
        );

        UIElement dialogueContainer = new UIElement().layout(layout -> layout
                .widthPercent(30)
                .heightPercent(100)
                .paddingBottom(30)
                .flexDirection(FlexDirection.COLUMN)
                .justifyContent(AlignContent.FLEX_END)
                .alignSelf(AlignItems.CENTER)
                .minWidth(300)
                .gapColumn(5)
        );

        ScrollerView dialogueBox = new ScrollerView();
        dialogueBox.layout(layout -> layout
                .widthPercent(100)
                .heightPercent(20)
                .alignSelf(AlignItems.CENTER)
        );
        dialogueBox.viewPort.layout(layout -> layout.paddingAll(0).paddingLeft(10).paddingRight(10).paddingTop(5).paddingBottom(5));
        dialogueBox.viewPort.style(style -> style.background(IGuiTexture.EMPTY));
        dialogueBox.style(style -> style.background(new RectTexture().setColor(0x8C2F2F31)));

        UIElement dialogueChoiceContainer = new UIElement();
        dialogueChoiceContainer.layout(layout -> layout.flexDirection(FlexDirection.ROW).gapAll(10).justifyContent(AlignContent.FLEX_END));

        Label nameLabel = new Label();
        nameLabel.setText(talkableName)
                .textStyle(textStyle -> textStyle.fontSize(18))
                .layout(layout -> layout.height(18).marginBottom(2));

        dialogueContainer.addChildren(nameLabel, dialogueBox, dialogueChoiceContainer);
        root.addChild(dialogueContainer);

        refs.dialogueBox = dialogueBox;
        refs.dialogueChoiceContainer = dialogueChoiceContainer;

        return ModularUI.of(UI.of(root));
    }

    private void showNode(DialogueNode node, Player player) {
        PlayerDialogueFlagsAttachment playerDialogueFlagsAttachment = player.getData(UnshatteredAttachments.PLAYER_DIALOGUE_FLAGS.get());

        refs.dialogueBox.clearAllScrollViewChildren();
        refs.dialogueBox.addScrollViewChild(
                new Label().setText(node.text()).textStyle(textStyle -> textStyle.textWrap(TextWrap.WRAP))
        );

        refs.dialogueChoiceContainer.clearAllChildren();

        List<DialogueChoice> available = node.dialogueChoices().stream()
                .filter(choice -> (choice.dialogueFlagRequirements().isEmpty() || choice.dialogueFlagRequirements().get().isSatisfied(playerDialogueFlagsAttachment)))
                .toList();

        if (available.isEmpty()) {
            refs.dialogueChoiceContainer.addChild(createClosingButton(Component.literal("...")));
            return;
        }

        if (available.size() == 1) {
            DialogueChoice choice = available.getFirst();
            Component text = (choice.text() == null || choice.text().getString().isEmpty())
                    ? Component.literal("...")
                    : choice.text();
            refs.dialogueChoiceContainer.addChild(
                    new Button().setText(text).setOnClick(_ -> {
                        selectChoice(choice, player);
                        triggerChoiceEvent(choice);
                    })
            );
            return;
        }

        for (DialogueChoice choice : available) {
            refs.dialogueChoiceContainer.addChild(
                    new Button().setText(choice.text()).setOnClick(_ -> {
                        selectChoice(choice, player);
                        triggerChoiceEvent(choice);
                    })
            );
        }
    }

    private void selectChoice(DialogueChoice choice, Player player) {
        Optional<DialogueNode> targetNode = choice.targetNode();
        ClientPacketDistributor.sendToServer(new QueueNewFlagsPacket(choice.setFlags()));

        if (targetNode.isEmpty()) {
            ClientPacketDistributor.sendToServer(new ResetFlagQueuePacket(true));
            Minecraft.getInstance().setScreen(null);
            return;
        }

        showNode(targetNode.get(), player);
    }

    private static Button createClosingButton(Component text) {
        return new Button().setText(text.getString().isEmpty() ? Component.literal("...") : text).setOnClick(_ -> {
            ClientPacketDistributor.sendToServer(new ResetFlagQueuePacket(true));
            Minecraft.getInstance().setScreen(null);
        });
    }

    private void triggerChoiceEvent(DialogueChoice choice) {
        if (choice.triggeredEvent().isPresent()) {
            ClientPacketDistributor.sendToServer(new TriggerEventPacket(choice.triggeredEvent().get()));
        }
    }
}