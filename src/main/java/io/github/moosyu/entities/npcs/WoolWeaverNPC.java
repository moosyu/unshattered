package io.github.moosyu.entities.npcs;

import io.github.moosyu.data.dialogue.DialogueInteractable;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class WoolWeaverNPC extends NPCEntity implements DialogueInteractable {
    public static final Identifier WOOL_WEAVER_IDENTIFIER = UnshatteredUtils.getUnshatteredIdentifier("wool_weaver");
    public static final Identifier INTRODUCTION_MESSAGE_IDENTIFIER = UnshatteredUtils.createDialogueNodeIdentifier(WOOL_WEAVER_IDENTIFIER, "introduction");
    public static final Identifier WOOLHEAD_IDENTIFIER = UnshatteredUtils.createDialogueNodeIdentifier(WOOL_WEAVER_IDENTIFIER, "woolhead");

    public WoolWeaverNPC(EntityType<? extends Mob> type, Level level) {
        super(type, level);
    }

    @Override
    public Identifier getInteractableIdentifier() {
        return WOOL_WEAVER_IDENTIFIER;
    }

    @Override
    public MutableComponent getInteractableName() {
        return getBoldName();
    }
}
