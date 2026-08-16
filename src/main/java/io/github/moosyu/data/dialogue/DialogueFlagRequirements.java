package io.github.moosyu.data.dialogue;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.moosyu.data.attachments.PlayerDialogueFlagsAttachment;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

import java.util.List;

public record DialogueFlagRequirements(List<Identifier> requiredFlags, List<Identifier> excludedFlags) {
    public static final Codec<DialogueFlagRequirements> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                Identifier.CODEC.listOf().optionalFieldOf("required_flags", List.of()).forGetter(DialogueFlagRequirements::requiredFlags),
                Identifier.CODEC.listOf().optionalFieldOf("excluded_flags", List.of()).forGetter(DialogueFlagRequirements::excludedFlags)
            ).apply(instance, DialogueFlagRequirements::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, DialogueFlagRequirements> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC.apply(ByteBufCodecs.list()), DialogueFlagRequirements::requiredFlags,
            Identifier.STREAM_CODEC.apply(ByteBufCodecs.list()), DialogueFlagRequirements::excludedFlags,
            DialogueFlagRequirements::new
    );

    public boolean isSatisfied(PlayerDialogueFlagsAttachment flagsAttachment) {
        return flagsAttachment.hasAllFlags(requiredFlags()) && excludedFlags().stream().noneMatch(flagsAttachment.getFlags()::contains);
    }
}
