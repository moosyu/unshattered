package io.github.moosyu.data.dialogue;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.List;

/**
 * the *roots* bdum tss to a dialogue tree
 * @param requiredFlags the required flags for this to be even an option
 * @param excludedFlags tracking flag/s required to not be active to have this option visible
 * @param priority the priority with higher number being higher priority to be selected. if two priorities are the same all bets are off and it's the first one seen
 * @param dialogueNode the dialogue node being moved to if this origin is followed
 * @param setFlags tracking flag/s (queued) to being added to the player after this is pressed. note that this will only be actually added once the dialogue has been completed so dont check for this in the same dialogue chain.
 */
public record DialogueTreeOrigin(List<Identifier> requiredFlags, List<Identifier> excludedFlags, int priority, DialogueNode dialogueNode, List<Identifier> setFlags) {
    public static final Codec<DialogueTreeOrigin> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Identifier.CODEC.listOf().optionalFieldOf("required_flags",List.of()).forGetter(DialogueTreeOrigin::requiredFlags),
                    Identifier.CODEC.listOf().optionalFieldOf("excluded_flags", List.of()).forGetter(DialogueTreeOrigin::excludedFlags),
                    Codec.INT.fieldOf("priority").forGetter(DialogueTreeOrigin::priority),
                    DialogueNode.CODEC.fieldOf("dialogue_node").forGetter(DialogueTreeOrigin::dialogueNode),
                    Identifier.CODEC.listOf().optionalFieldOf("set_flags", List.of()).forGetter(DialogueTreeOrigin::setFlags)
            ).apply(instance, DialogueTreeOrigin::new)
    );
}