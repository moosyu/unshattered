package io.github.moosyu.data.drops;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.moosyu.data.attachments.PlayerSkillsAttachment;

import java.util.List;

public record BlockBreakData(PlayerSkillsAttachment.Skill skill, float expAmount, List<DropData> dropData) {
    public static Codec<BlockBreakData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    PlayerSkillsAttachment.Skill.CODEC.fieldOf("skill").forGetter(BlockBreakData::skill),
                    Codec.FLOAT.fieldOf("exp_amount").forGetter(BlockBreakData::expAmount),
                    DropData.CODEC.listOf().fieldOf("drop_data").forGetter(BlockBreakData::dropData)
            ).apply(instance, BlockBreakData::new));
}