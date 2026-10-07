package io.github.moosyu.entities.npcs;

import io.github.moosyu.data.dialogue.DialogueInteractable;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class BubuNPC extends NPCEntity implements DialogueInteractable {
    public static final Identifier BUBU_IDENTIFIER = UnshatteredUtils.getUnshatteredIdentifier("bubu");

    public BubuNPC(EntityType<? extends Mob> type, Level level) {
        super(type, level, UnshatteredUtils.PURPLE);
    }

    @Override
    public Identifier getInteractableIdentifier() {
        return BUBU_IDENTIFIER;
    }

    @Override
    public Component getInteractableName() {
        return getStyledName();
    }
}
