package io.github.moosyu.entities.npcs;

import io.github.moosyu.data.dialogue.DialogueInteractable;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class JotraelineGreatforgeNPC extends NPCEntity implements DialogueInteractable {
    public static final Identifier JOTRAELINE_GREATFORGE_IDENTIFIER = UnshatteredUtils.getUnshatteredIdentifier("jotraeline_greatforge");
    public static final Identifier INTRODUCTION_MESSAGE_IDENTIFIER = UnshatteredUtils.createDialogueNodeIdentifier(JOTRAELINE_GREATFORGE_IDENTIFIER, "introduction");
    public static final Identifier ANGRY_IDENTIFIER = UnshatteredUtils.createDialogueNodeIdentifier(JOTRAELINE_GREATFORGE_IDENTIFIER, "angry");
    public static final Identifier APOLOGY_TOUR = UnshatteredUtils.getUnshatteredIdentifier("apology_tour");

    public JotraelineGreatforgeNPC(EntityType<? extends Mob> type, Level level) {
        super(type, level);
    }

    @Override
    public Identifier getInteractableIdentifier() {
        return JOTRAELINE_GREATFORGE_IDENTIFIER;
    }

    @Override
    public MutableComponent getInteractableName() {
        return getBoldName();
    }
}
