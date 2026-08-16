package io.github.moosyu.blocks;

import io.github.moosyu.data.dialogue.*;
import io.github.moosyu.util.DialogueUtil;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

import java.util.List;

import static io.github.moosyu.Unshattered.MODID;

public class TestBlock extends Block implements DialogueInteractable {
    public static final Component name = Component.literal("Rock");
    public static final Identifier ROCK_DIALOGUE_TREE = DialogueUtil.createDialogueTreeIdentifier(name.getString().toLowerCase(), "rock_dialogue_tree");
    public static final Identifier HI_MESSAGE_IDENTIFIER = DialogueUtil.createDialogueNodeIdentifier(ROCK_DIALOGUE_TREE, name.getString(), "hi");
    public static final Identifier HI2_MESSAGE_IDENTIFIER = DialogueUtil.createDialogueNodeIdentifier(ROCK_DIALOGUE_TREE, name.getString(), "hi_2");
    public static final Identifier ROCKS_QUEST = Identifier.fromNamespaceAndPath(MODID, "rocks_quest");

    public TestBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Component getInteractableName() {
        return name;
    }

    @Override
    public DialogueTree getDialogueTree(RegistryAccess registryAccess) {
        return DialogueUtil.getDialogueTreeObject(registryAccess, ROCK_DIALOGUE_TREE);
    }
}
