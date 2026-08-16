package io.github.moosyu.collectables.rewards;

import io.github.moosyu.data.attachments.PlayerSkillsAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import net.minecraft.world.entity.player.Player;

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
}
