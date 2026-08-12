package io.github.moosyu.data.dialogue;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

/**
 * the *roots* bdum tss to a dialogue tree
 * @param requiredFlags the required flags for this to be even an option
 * @param priority the priority with higher number being higher priority to be selected. if two priorities are the same all bets are off and it's the first one seen
 * @param dialogueNode the dialogue node being moved to if this origin is followed
 */
public record DialogueTreeOrigin(List<String> requiredFlags, int priority, DialogueNode dialogueNode) {
    public static final Codec<DialogueTreeOrigin> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.listOf().optionalFieldOf("required_flags", List.of()).forGetter(DialogueTreeOrigin::requiredFlags),
                    Codec.INT.fieldOf("priority").forGetter(DialogueTreeOrigin::priority),
                    DialogueNode.CODEC.fieldOf("dialogue_node").forGetter(DialogueTreeOrigin::dialogueNode)
            ).apply(instance, DialogueTreeOrigin::new)
    );
}