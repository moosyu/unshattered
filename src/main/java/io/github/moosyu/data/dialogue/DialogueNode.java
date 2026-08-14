package io.github.moosyu.data.dialogue;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;

/**
 * nodes used to construct a dialogue tree
 * @param text the text being displayed.
 * @param dialogueChoices the possible choices for the next dialogue. leaving list empty or if  choice text is nothing it becomes ...
 */
public record DialogueNode(Component text, List<DialogueChoice> dialogueChoices) {
    public static final Codec<DialogueNode> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ComponentSerialization.CODEC.fieldOf("text").forGetter(DialogueNode::text),
                    DialogueChoice.CODEC.listOf().fieldOf("dialogue_choices").forGetter(DialogueNode::dialogueChoices)
            ).apply(instance, DialogueNode::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, DialogueNode> STREAM_CODEC = StreamCodec.composite(
                    ComponentSerialization.STREAM_CODEC, DialogueNode::text,
                    DialogueChoice.STREAM_CODEC.apply(ByteBufCodecs.list()), DialogueNode::dialogueChoices,
                    DialogueNode::new
            );
}
