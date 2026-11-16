package io.github.moosyu.entities.npcs;

import io.github.moosyu.data.dialogue.DialogueInteractable;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class Einary extends NPCEntity implements DialogueInteractable {
    public static final Identifier EINARY_IDENTIFIER = UnshatteredUtils.getUnshatteredIdentifier("einary");
    public static final Identifier EIANARY_INTRODUCTION = UnshatteredUtils.getUnshatteredIdentifier("einary_introduction");

    public Einary(EntityType<? extends Mob> type, Level level) {
        super(type, level);
    }

    @Override
    public Identifier getInteractableIdentifier() {
        return EINARY_IDENTIFIER;
    }

    @Override
    public MutableComponent getInteractableName() {
        return getBoldName();
    }
}
