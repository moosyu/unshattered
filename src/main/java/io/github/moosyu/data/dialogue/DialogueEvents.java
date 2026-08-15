package io.github.moosyu.data.dialogue;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;

import static io.github.moosyu.Unshattered.MODID;

// todo: put this in some sort of data driven setup
public final class DialogueEvents {
    public static final Identifier ROCK_COMPLETE_EVENT = Identifier.fromNamespaceAndPath(MODID, "rock_complete_event");

    private static final Map<Identifier, DialogueTriggeredEvent> DIALOGUE_EVENTS = new HashMap<>(
            Map.of(ROCK_COMPLETE_EVENT, new GiveItemDialogueEvent(BuiltInRegistries.ITEM.wrapAsHolder(Items.DIAMOND), 1)
            )
    );

    public static DialogueTriggeredEvent getDialogueEvent(Identifier dialogueEventIdentifier) {
        return DIALOGUE_EVENTS.get(dialogueEventIdentifier);
    }
}
