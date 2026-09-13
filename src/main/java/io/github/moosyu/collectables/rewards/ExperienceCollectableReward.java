package io.github.moosyu.collectables.rewards;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.moosyu.data.attachments.PlayerSkillsAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.dialogue.GiveItemDialogueEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

public record ExperienceCollectableReward(float amount, PlayerSkillsAttachment.Skill experienceType) implements CollectableReward {
    @Override
    public RewardCategories category() {
        return RewardCategories.EXPERIENCE;
    }

    @Override
    public void reward(Player player) {
        PlayerSkillsAttachment skills = player.getData(UnshatteredAttachments.PLAYER_SKILLS.get());
        skills.addExp(this.experienceType(), this.amount(), player);
    }

    @Override
    public MapCodec<? extends ExperienceCollectableReward> codec() {
        return CODEC;
    }

    public static final MapCodec<ExperienceCollectableReward> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("amount").forGetter(ExperienceCollectableReward::amount),
                    PlayerSkillsAttachment.Skill.CODEC.fieldOf("experience_type").forGetter(ExperienceCollectableReward::experienceType)
            ).apply(instance, ExperienceCollectableReward::new)
    );
}
