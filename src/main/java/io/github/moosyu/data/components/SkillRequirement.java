package io.github.moosyu.data.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.moosyu.attachments.PlayerSkillsAttachment;

public record SkillRequirement(PlayerSkillsAttachment.Skill skill, int level) {
    public static final Codec<SkillRequirement> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.xmap(PlayerSkillsAttachment.Skill::valueOf, PlayerSkillsAttachment.Skill::name).fieldOf("skill").forGetter(SkillRequirement::skill),
            Codec.INT.fieldOf("level").forGetter(SkillRequirement::level)).apply(instance, SkillRequirement::new));
}
