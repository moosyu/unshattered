package io.github.moosyu.blocks;

import io.github.moosyu.data.dialogue.DialogueInteractable;
import io.github.moosyu.data.dialogue.DialogueTreeOrigin;
import io.github.moosyu.events.DataPackRegistryHandler;
import io.github.moosyu.util.DialogueUtil;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Objects;

public class TestBlock extends Block implements DialogueInteractable {
    public static final Component name = Component.literal("Rock");
    public static final Identifier HI_MESSAGE_IDENTIFIER = DialogueUtil.createDialogueNodeIdentifier(name.getString().toLowerCase(), "hi");
    public static final Identifier HI_2_MESSAGE_IDENTIFIER = DialogueUtil.createDialogueNodeIdentifier(name.getString().toLowerCase(), "hi2");

    public TestBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Component getInteractableName() {
        return name;
    }

    @Override
    public List<DialogueTreeOrigin> getDialogueTreeOrigins(RegistryAccess registryAccess) {
        return List.of(
                DialogueUtil.getDialogueTreeOriginObject(registryAccess, HI_MESSAGE_IDENTIFIER),
                DialogueUtil.getDialogueTreeOriginObject(registryAccess, HI_2_MESSAGE_IDENTIFIER)
        );
    }
}
