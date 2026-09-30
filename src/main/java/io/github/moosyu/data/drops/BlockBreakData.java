package io.github.moosyu.data.drops;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.moosyu.data.attachments.PlayerPowderAttachment;
import io.github.moosyu.data.attachments.PlayerSkillsAttachment;

import java.util.List;
import java.util.Optional;

public record BlockBreakData(PlayerSkillsAttachment.Skill skill, float expAmount, List<DropData> dropData, Optional<PlayerPowderAttachment.Powder> powder) {
    public BlockBreakData(PlayerSkillsAttachment.Skill skill, float expAmount, List<DropData> dropData) {
        this(skill, expAmount, dropData, Optional.empty());
    }

    public static Codec<BlockBreakData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    PlayerSkillsAttachment.Skill.CODEC.fieldOf("skill").forGetter(BlockBreakData::skill),
                    Codec.FLOAT.fieldOf("exp_amount").forGetter(BlockBreakData::expAmount),
                    DropData.CODEC.listOf().fieldOf("drop_data").forGetter(BlockBreakData::dropData),
                    PlayerPowderAttachment.Powder.CODEC.optionalFieldOf("powder").forGetter(BlockBreakData::powder)
            ).apply(instance, BlockBreakData::new)
    );
}