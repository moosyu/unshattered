package io.github.moosyu.entities;

import io.github.moosyu.data.dialogue.DialogueInteractable;
import io.github.moosyu.data.dialogue.DialogueTree;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class JotraelineGreatforgeNPC extends NPCEntity implements DialogueInteractable {
    public static final Identifier JOTRAELINE_GREATFORGE_DIALOGUE_TREE = UnshatteredUtils.createDialogueTreeIdentifier("jotraeline_greatforge");
    public static final Identifier INTRODUCTION_MESSAGE_IDENTIFIER = UnshatteredUtils.createDialogueNodeIdentifier(JOTRAELINE_GREATFORGE_DIALOGUE_TREE, "introduction");
    public static final Identifier ANGRY_IDENTIFIER = UnshatteredUtils.createDialogueNodeIdentifier(JOTRAELINE_GREATFORGE_DIALOGUE_TREE, "angry");
    public static final Identifier APOLOGY_TOUR = UnshatteredUtils.getUnshatteredIdentifier("apology_tour");

    public JotraelineGreatforgeNPC(EntityType<? extends Mob> type, Level level) {
        super(type, level, 0xFF00AA00);
    }

    @Override
    public Component getInteractableName() {
        return getName().copy().withColor(getNametagColour()).withStyle(ChatFormatting.BOLD);
    }

    @Override
    public DialogueTree getDialogueTree(RegistryAccess registryAccess) {
        return UnshatteredUtils.getDialogueTreeObject(registryAccess, JOTRAELINE_GREATFORGE_DIALOGUE_TREE);
    }
}
