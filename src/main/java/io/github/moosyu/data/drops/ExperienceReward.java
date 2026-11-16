package io.github.moosyu.data.drops;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.moosyu.data.attachments.PlayerSkillsAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.Nullable;

public record ExperienceReward(PlayerSkillsAttachment.Skill skill, float experienceAmount) implements DropReward {
    public static final MapCodec<ExperienceReward> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            PlayerSkillsAttachment.Skill.CODEC.fieldOf("skill").forGetter(ExperienceReward::skill),
            Codec.FLOAT.fieldOf("experience_amount").forGetter(ExperienceReward::experienceAmount)
    ).apply(instance, ExperienceReward::new));

    @Override
    public Type type() {
        return Type.EXPERIENCE;
    }

    @Override
    public float grant(ServerPlayer player, RandomSource random, @Nullable Double fortuneAmount, float modifiedPercentage) {
        PlayerSkillsAttachment skills = player.getData(UnshatteredAttachments.PLAYER_SKILLS.get());
        skills.addExp(skill, experienceAmount * modifiedPercentage, player);

        return experienceAmount;
    }
}