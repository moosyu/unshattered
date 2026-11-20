package io.github.moosyu.entities.npc;

import io.github.moosyu.data.dialogue.DialogueInteractable;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class Forger extends NPCEntity implements DialogueInteractable {
    public static final Identifier FORGER_IDENTIFIER = UnshatteredUtils.getUnshatteredIdentifier("forger");
    public static final Identifier FORGER_INTRODUCTION = UnshatteredUtils.getUnshatteredIdentifier("forger_introduction");

    public Forger(EntityType<? extends Mob> type, Level level) {
        super(type, level);
    }

    @Override
    public Identifier getInteractableIdentifier() {
        return FORGER_IDENTIFIER;
    }

    @Override
    public MutableComponent getInteractableName() {
        return getBoldName();
    }
}
