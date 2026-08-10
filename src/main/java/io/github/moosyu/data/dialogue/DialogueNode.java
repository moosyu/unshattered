package io.github.moosyu.data.dialogue;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;

import java.util.List;

/**
 * nodes used to construct a dialogue tree
 * @param text the text being displayed
 * @param dialogueChoices the possible choices for the next dialogue. <bold>if there is just one choice AND its text is empty it wont be displayed but its effects will still trigger.</bold>
 */
public record DialogueNode(Component text, List<DialogueChoice> dialogueChoices) {
    public static final Codec<DialogueNode> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ComponentSerialization.CODEC.fieldOf("text").forGetter(DialogueNode::text),
                    DialogueChoice.CODEC.listOf().fieldOf("dialogue_choices").forGetter(DialogueNode::dialogueChoices)
            ).apply(instance, DialogueNode::new)
    );
}
