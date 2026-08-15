package io.github.moosyu.data.dialogue;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

import java.util.List;
import java.util.Optional;

/**
 * a player's dialogue choice options
 * @param text the text shown as the player's response
 * @param targetNode whatever dialogue node should be moved to if this choice is selected. optional so if there is no target then if pressed it ends dialogue.
 * @param dialogueFlagRequirements tracking flag/s required to have this option visible or not
 * @param setFlags tracking flag/s (queued) to being added to the player after this is pressed. note that this will only be actually added once the dialogue has been completed so don't check for this in the same dialogue chain.
 * @param triggeredEvent id for event to be queued to run if this option is pressed. note that this will only be actually added once the dialogue has been completed.
 */
public record DialogueChoice(Component text, Optional<DialogueNode> targetNode, Optional<DialogueFlagRequirements> dialogueFlagRequirements, List<Identifier> setFlags, Optional<Identifier> triggeredEvent) {
    public DialogueChoice(Component text, DialogueNode targetNode) {
        this(text, Optional.of(targetNode), Optional.empty(), List.of(), Optional.empty());
    }

    public DialogueChoice(Component text) {
        this(text, Optional.empty(), Optional.empty(), List.of(), Optional.empty());
    }

    public DialogueChoice(Component text, Identifier triggeredEvent) {
        this(text, Optional.empty(), Optional.empty(), List.of(), Optional.of(triggeredEvent));
    }

    public static final Codec<DialogueChoice> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ComponentSerialization.CODEC.fieldOf("text").forGetter(DialogueChoice::text),
                    // dialogue node requires dialogue choice's codec so if it isnt lazy initialised stuff gets wild
                    Codec.lazyInitialized(() -> DialogueNode.CODEC).optionalFieldOf("target_node").forGetter(DialogueChoice::targetNode),
                    DialogueFlagRequirements.CODEC.optionalFieldOf("dialogue_flag_requirements").forGetter(DialogueChoice::dialogueFlagRequirements),
                    Identifier.CODEC.listOf().optionalFieldOf("set_flags", List.of()).forGetter(DialogueChoice::setFlags),
                    Identifier.CODEC.optionalFieldOf("action").forGetter(DialogueChoice::triggeredEvent)
            ).apply(instance, DialogueChoice::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, DialogueChoice> STREAM_CODEC = StreamCodec.composite(
            ComponentSerialization.STREAM_CODEC, DialogueChoice::text,
            ByteBufCodecs.optional(NeoForgeStreamCodecs.lazy(() -> DialogueNode.STREAM_CODEC)), DialogueChoice::targetNode,
            ByteBufCodecs.optional(DialogueFlagRequirements.STREAM_CODEC), DialogueChoice::dialogueFlagRequirements,
            Identifier.STREAM_CODEC.apply(ByteBufCodecs.list()), DialogueChoice::setFlags,
            ByteBufCodecs.optional(Identifier.STREAM_CODEC), DialogueChoice::triggeredEvent,
            DialogueChoice::new
    );
}