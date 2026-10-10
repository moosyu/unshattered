package io.github.moosyu.entities.npcs;

import io.github.moosyu.data.dialogue.DialogueInteractable;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class JennyNPC extends NPCEntity implements DialogueInteractable {
    public static final Identifier JENNY_IDENTIFIER = UnshatteredUtils.getUnshatteredIdentifier("jenny");
    public static final Identifier JENNY_INTRODUCTION = UnshatteredUtils.getUnshatteredIdentifier("jenny_introduction");

    public JennyNPC(EntityType<? extends Mob> type, Level level) {
        super(type, level, 0xFFFFFFFF);
    }

    @Override
    public Identifier getInteractableIdentifier() {
        return JENNY_IDENTIFIER;
    }

    @Override
    public Component getInteractableName() {
        return getStyledName();
    }
}
