package io.github.moosyu.data.dialogue;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import java.util.List;
import java.util.Optional;

import static io.github.moosyu.events.DataPackRegistryHandler.DIALOGUE_NODE_REGISTRY_KEY;

/**
 * a player's dialogue choice options
 * @param text the text shown as the player's response
 * @param targetNode whatever dialogue node should be moved to if this choice is selected
 * @param requiredFlags tracking flag/s required to have this option visible
 * @param excludedFlags tracking flag/s required to not be active to have this option visible
 * @param setFlags tracking flag/s (queued) to being added to the player after this is pressed. note that this will only be actually added once the dialogue has been completed so dont check for this in the same dialogue chain.
 * @param triggeredEvent id for event to be queued to run if this option is pressed. note that this will only be actually added once the dialogue has been completed.
 */
public record DialogueChoice(Component text, ResourceKey<DialogueNode> targetNode, List<Identifier> requiredFlags, List<Identifier> excludedFlags, List<Identifier> setFlags, Optional<Identifier> triggeredEvent) {
    public static final Codec<DialogueChoice> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ComponentSerialization.CODEC.fieldOf("text").forGetter(DialogueChoice::text),
                    ResourceKey.codec(DIALOGUE_NODE_REGISTRY_KEY).fieldOf("target_node").forGetter(DialogueChoice::targetNode),
                    Identifier.CODEC.listOf().optionalFieldOf("required_flags", List.of()).forGetter(DialogueChoice::requiredFlags),
                    Identifier.CODEC.listOf().optionalFieldOf("excluded_flags", List.of()).forGetter(DialogueChoice::excludedFlags),
                    Identifier.CODEC.listOf().optionalFieldOf("set_flags", List.of()).forGetter(DialogueChoice::setFlags),
                    Identifier.CODEC.optionalFieldOf("action").forGetter(DialogueChoice::triggeredEvent)
            ).apply(instance, DialogueChoice::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, DialogueChoice> STREAM_CODEC = StreamCodec.composite(
            ComponentSerialization.STREAM_CODEC, DialogueChoice::text,
            ResourceKey.streamCodec(DIALOGUE_NODE_REGISTRY_KEY), DialogueChoice::targetNode,
            Identifier.STREAM_CODEC.apply(ByteBufCodecs.list()), DialogueChoice::requiredFlags,
            Identifier.STREAM_CODEC.apply(ByteBufCodecs.list()), DialogueChoice::excludedFlags,
            Identifier.STREAM_CODEC.apply(ByteBufCodecs.list()), DialogueChoice::setFlags,
            ByteBufCodecs.optional(Identifier.STREAM_CODEC), DialogueChoice::triggeredEvent,
            DialogueChoice::new
    );
}