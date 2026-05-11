package io.github.moosyu.attachments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class PlayerSkillsAttachment {
    public PlayerSkillsAttachment(float miningExp, float combatExp, float farmingExp, float fishingExp, float foragingExp) {
        this.miningExp = miningExp;
        this.combatExp = combatExp;
        this.fishingExp = fishingExp;
        this.foragingExp = foragingExp;
        this.farmingExp = farmingExp;
    }

    public void transferSkills(PlayerSkillsAttachment other) {
        this.miningExp = other.miningExp;
        this.combatExp = other.combatExp;
        this.farmingExp = other.farmingExp;
        this.fishingExp = other.fishingExp;
        this.foragingExp = other.foragingExp;
    }

    private float miningExp;
    private float combatExp;
    private float farmingExp;
    private float fishingExp;
    private float foragingExp;

    public float getMiningExp() {
        return miningExp;
    }

    public void addMiningExp(float amount) {
        miningExp += amount;
    }

    public float getCombatExp() {
        return combatExp;
    }

    public void addCombatExp(float amount) {
        combatExp += amount;
    }

    public float getFarmingExp() {
        return farmingExp;
    }

    public void addFarmingExp(float amount) {
        farmingExp += amount;
    }

    public float getFishingExp() {
        return fishingExp;
    }

    public void addFishingExp(float amount) {
        fishingExp += amount;
    }

    public float getForagingExp() {
        return foragingExp;
    }

    public void addForagingExp(float amount) {
        foragingExp += amount;
    }

    public static final Codec<PlayerSkillsAttachment> RECORD_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.FLOAT.fieldOf("miningExp").forGetter(PlayerSkillsAttachment::getMiningExp),
                    Codec.FLOAT.fieldOf("combatExp").forGetter(PlayerSkillsAttachment::getCombatExp),
                    Codec.FLOAT.fieldOf("farmingExp").forGetter(PlayerSkillsAttachment::getFarmingExp),
                    Codec.FLOAT.fieldOf("fishingExp").forGetter(PlayerSkillsAttachment::getFishingExp),
                    Codec.FLOAT.fieldOf("foragingExp").forGetter(PlayerSkillsAttachment::getForagingExp)).apply(instance, PlayerSkillsAttachment::new)
    );
}
