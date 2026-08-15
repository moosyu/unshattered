package io.github.moosyu.data.dialogue;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.Optional;

/**
 * the *roots* bdum tss to a dialogue tree
 * @param priority the priority with higher number being higher priority to be selected. if two priorities are the same all bets are off and it's the first one seen
 * @param dialogueNode the dialogue node being moved to if this origin is followed
 * @param dialogueFlagRequirements tracking flag/s required to have this dialogue tree can be ran through
 * @param setFlags tracking flag/s (queued) to being added to the player after this is pressed. note that this will only be actually added once the dialogue has been completed so dont check for this in the same dialogue chain.
 */
public record DialogueTreeOrigin(int priority, DialogueNode dialogueNode, Optional<DialogueFlagRequirements> dialogueFlagRequirements, List<Identifier> setFlags) {
    public DialogueTreeOrigin(int priority, DialogueNode dialogueNode) {
        this(priority, dialogueNode, Optional.empty(), List.of());
    }

    public DialogueTreeOrigin(int priority, DialogueNode dialogueNode, List<Identifier> setFlags) {
        this(priority, dialogueNode, Optional.empty(), setFlags);
    }

    public static final Codec<DialogueTreeOrigin> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("priority").forGetter(DialogueTreeOrigin::priority),
                    DialogueNode.CODEC.fieldOf("dialogue_node").forGetter(DialogueTreeOrigin::dialogueNode),
                    DialogueFlagRequirements.CODEC.optionalFieldOf("dialogue_flag_requirements").forGetter(DialogueTreeOrigin::dialogueFlagRequirements),
                    Identifier.CODEC.listOf().optionalFieldOf("set_flags", List.of()).forGetter(DialogueTreeOrigin::setFlags)
            ).apply(instance, DialogueTreeOrigin::new)
    );
}