package io.github.moosyu.gui.screens;

import io.github.moosyu.data.dialogue.DialogueChoice;
import io.github.moosyu.data.dialogue.DialogueNode;
import io.github.moosyu.packets.QueueNewFlagsPacket;
import io.github.moosyu.packets.ResetFlagQueuePacket;
import io.github.moosyu.packets.TriggerEventPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

public class DialogueScreen extends Screen {
    Component talkableName;
    Player player;
    DialogueNode selectedDialogueNode;
    private static final int DIALOGUE_TEXTBOX_WIDTH = 100;
    private static final int DIALOGUE_TEXTBOX_HEIGHT = 50;
    private static final int BOTTOM_Y_OFFSET = 30;

    public DialogueScreen(Component talkableName, DialogueNode selectedDialogueNode, Player player) {
        super(Component.translatable("screen.unshattered.dialogue"));

        this.talkableName = talkableName;
        this.selectedDialogueNode = selectedDialogueNode;
        this.player = player;
    }

    @Override
    public void extractBackground(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        int centerXPos = graphics.guiWidth() / 2;
        graphics.fill(centerXPos - (DIALOGUE_TEXTBOX_WIDTH / 2),
                graphics.guiHeight() - DIALOGUE_TEXTBOX_HEIGHT - BOTTOM_Y_OFFSET,
                centerXPos + (DIALOGUE_TEXTBOX_WIDTH / 2),
                graphics.guiHeight() - BOTTOM_Y_OFFSET,
                0x8C2F2F31
        );
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        Font font = Minecraft.getInstance().font;
        graphics.text(font,
                talkableName,
                (graphics.guiWidth() / 2) + (DIALOGUE_TEXTBOX_WIDTH / 2),
                graphics.guiHeight() - DIALOGUE_TEXTBOX_HEIGHT - BOTTOM_Y_OFFSET - font.lineHeight,
                0xFFFFFFFF
        );
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
    }

    private void selectChoice(DialogueChoice choice, Player player) {
        Optional<DialogueNode> targetNode = choice.targetNode();
        ClientPacketDistributor.sendToServer(new QueueNewFlagsPacket(choice.setFlags()));

        if (targetNode.isEmpty()) {
            ClientPacketDistributor.sendToServer(new ResetFlagQueuePacket(true));
            Minecraft.getInstance().setScreen(null);
            return;
        }

        // showNode(targetNode.get(), player);
    }

    private static Button createClosingButton(Component text) {
        return Button.builder(text.getString().isEmpty() ? Component.literal("...") : text, _ -> {
            ClientPacketDistributor.sendToServer(new ResetFlagQueuePacket(true));
            Minecraft.getInstance().setScreen(null);
        }).build();
    }

    private void triggerChoiceEvent(DialogueChoice choice) {
        if (choice.triggeredEvent().isPresent()) {
            ClientPacketDistributor.sendToServer(new TriggerEventPacket(choice.triggeredEvent().get()));
        }
    }
}