package io.github.moosyu.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.moosyu.attachments.PlayerSkillsAttachment;

import java.util.List;

/**
 * record for all drops able to be rewarded when killing mobs
 * @param drops list of mob item drop data
 * @param minCoins minimum coins able to be rewarded
 * @param maxCoins maximum amount of coins able to be rewarded
 * @param skill skill to have experience rewarded to
 * @param experience amount of experience to be rewarded
 */
public record MobRewardData(List<MobItemDropData> drops, int minCoins, int maxCoins, PlayerSkillsAttachment.Skill skill, float experience) {
    /**
     * constructor for non-deviating coin amounts
     * @param drops list of mob item drop data
     * @param coins amount of coins able to be rewarded
     * @param skill skill to have experience rewarded to
     * @param experience amount of experience to be rewarded
     */
    public MobRewardData(List<MobItemDropData> drops, int coins, PlayerSkillsAttachment.Skill skill, float experience) {
        this(drops, coins, coins, skill, experience);
    }

    public static final Codec<MobRewardData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            MobItemDropData.CODEC.listOf().fieldOf("drops").forGetter(MobRewardData::drops),
            Codec.INT.fieldOf("min_coins").forGetter(MobRewardData::minCoins),
            Codec.INT.fieldOf("max_coins").forGetter(MobRewardData::maxCoins),
            PlayerSkillsAttachment.Skill.CODEC.fieldOf("skill").forGetter(MobRewardData::skill),
            Codec.FLOAT.fieldOf("experience").forGetter(MobRewardData::experience)
    ).apply(instance, MobRewardData::new));
}
