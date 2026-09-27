package io.github.moosyu.data.dialogue;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;
import java.util.Optional;

/**
 * nodes used to construct a dialogue tree
 * @param text the text being displayed.
 * @param dialogueChoices the possible choices for the next dialogue. if there are no choices or choice text is nothing it becomes ...
 * @param interactedSpeaking whether it displays the interacted name displays or the player's name
 */
public record DialogueNode(Component text, Optional<List<DialogueChoice>> dialogueChoices, boolean interactedSpeaking) {
    public DialogueNode(Component text) {
        this(text, Optional.empty(), true);
    }

    public DialogueNode(Component text, boolean interactedSpeaking, DialogueChoice... dialogueChoices) {
        this(text, Optional.of(List.of(dialogueChoices)), interactedSpeaking);
    }

    public static final Codec<DialogueNode> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ComponentSerialization.CODEC.fieldOf("text").forGetter(DialogueNode::text),
                    DialogueChoice.CODEC.listOf().optionalFieldOf("dialogue_choices").forGetter(DialogueNode::dialogueChoices),
                    Codec.BOOL.fieldOf("interacted_speaking").forGetter(DialogueNode::interactedSpeaking)
            ).apply(instance, DialogueNode::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, DialogueNode> STREAM_CODEC = StreamCodec.composite(
                    ComponentSerialization.STREAM_CODEC, DialogueNode::text,
                    DialogueChoice.STREAM_CODEC.apply(ByteBufCodecs.list()).apply(ByteBufCodecs::optional), DialogueNode::dialogueChoices,
                    ByteBufCodecs.BOOL, DialogueNode::interactedSpeaking,
                    DialogueNode::new
            );
}
