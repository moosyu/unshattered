package io.github.moosyu.data.dialogue;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record DialogueTree(List<DialogueTreeOrigin> dialogueTreeOrigins) {
    public static final Codec<DialogueTree> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(DialogueTreeOrigin.CODEC.listOf().fieldOf("dialogue_tree_origins").forGetter(DialogueTree::dialogueTreeOrigins)).apply(instance, DialogueTree::new)
    );
}
