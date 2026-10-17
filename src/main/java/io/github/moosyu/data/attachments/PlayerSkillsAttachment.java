package io.github.moosyu.data.attachments;

import com.mojang.serialization.Codec;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.packets.ExpSoundEffectPacket;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;

public final class PlayerSkillsAttachment {
    public record LevelReward(UnshatteredAttributeValues attribute, double amount) {
        public Component getComponent() {
            return Component.literal("    " + ((amount == Math.floor(amount)) ? String.valueOf((long) amount) : String.valueOf(amount)) + (attribute.percentage ? "%" : "")).withColor(0xFF00FF24)
                    .append(Component.literal(" " + attribute.symbol + " ").withColor(attribute.color))
                    .append(Component.translatable(attribute.getTranslationKey()).withColor(attribute.color));
        }
    }

    /**
     * cumulative skill exps
     */
    private static final int[] SKILL_LEVEL_TABLE = {
            50,
            175,
            375,
            675,
            1175,
            1925,
            2925,
            4425,
            6425,
            9925,
            14925,
            22425,
            32425,
            47425,
            67425,
            97425,
            147425,
            222425,
            322425,
            522425,
            822425,
            1222425,
            1722425,
            2322425,
            3022425,
            3822425,
            4722425,
            5722425,
            6822425,
            8022425,
            9322425,
            10722425,
            12222425,
            13822425,
            15522425,
            17322425,
            19222425,
            21222425,
            23322425,
            25522425,
            27822425,
            30222425,
            32722425,
            35322425,
            38072425,
            40972425,
            44072425,
            47472425,
            51172425,
            55172425
    };

    /**
     * coins per level (not cumulative)
     */
    private static final int[] COINS_LEVEL_TABLE = {
            100,
            250,
            500,
            750,
            1000,
            2000,
            3000,
            4000,
            5000,
            7500,
            10000,
            15000,
            20000,
            25000,
            30000,
            40000,
            50000,
            65000,
            80000,
            100000,
            125000,
            150000,
            175000,
            200000,
            225000,
            250000,
            275000,
            300000,
            325000,
            350000,
            375000,
            400000,
            425000,
            450000,
            475000,
            500000,
            550000,
            600000,
            650000,
            700000,
            750000,
            800000,
            850000,
            900000,
            1000000,
            1000000,
            1000000,
            1000000,
            1000000,
            1000000
    };
    private final float[] skillExp = new float[Skill.values().length];

    public enum Skill implements StringRepresentable {
        COMBAT("combat", _ -> List.of(
                new LevelReward(UnshatteredAttributeValues.CRITICAL_DAMAGE, 10.0d),
                new LevelReward(UnshatteredAttributeValues.CRITICAL_CHANCE, 1.0d)
        )),
        FARMING("farming", level -> List.of(
                new LevelReward(UnshatteredAttributeValues.HEALTH,
                        level < 15 ? 1.0d : level < 20 ? 2.0d : level < 25 ? 3.0d : 4.0d),
                new LevelReward(UnshatteredAttributeValues.FARMING_FORTUNE, 2.0d)
        )),
        FISHING("fishing", _ -> List.of(
                new LevelReward(UnshatteredAttributeValues.FISHING_SPEED, 1.0d),
                new LevelReward(UnshatteredAttributeValues.FISHING_FORTUNE, 0.1d)
        )),
        MINING("mining", level -> List.of(
                new LevelReward(UnshatteredAttributeValues.DEFENCE, level < 15 ? 1.0d : 2.0d),
                new LevelReward(UnshatteredAttributeValues.MINING_FORTUNE, 2.0d)
        )),
        FORAGING("foraging", level -> List.of(
                new LevelReward(UnshatteredAttributeValues.STRENGTH, level < 15 ? 1.0d : 2.0d),
                new LevelReward(UnshatteredAttributeValues.FARMING_FORTUNE, 2.0d)
        )),
        MAGECRAFT("magecraft", _ -> List.of(
                new LevelReward(UnshatteredAttributeValues.MANA, 2.0d),
                new LevelReward(UnshatteredAttributeValues.MANA_REGEN, 1.5d)
        )),
        CARPENTRY("carpentry", _ -> List.of(
                new LevelReward(UnshatteredAttributeValues.HEALTH, 1.0d)
        ));

        private final String id;
        private final IntFunction<List<LevelReward>> rewards;

        Skill(String id, IntFunction<List<LevelReward>> rewards) {
            this.id = id;
            this.rewards = rewards;
        }

        public List<LevelReward> getRewards(int level) {
            return rewards.apply(level);
        }

        /**
         * @param levelIndex intended level - 1 so for the next level use "current level" number here
         */
        public int getCoinReward(int levelIndex) {
            return COINS_LEVEL_TABLE[levelIndex];
        }

        public String getTranslationKey() {
            return "skills.name.unshattered." + id;
        }

        /**
         * modifies the base attribute value and sends a reward message
         * @param player player having attribute added
         * @param attribute attribute being added to
         * @param amount amount added to attribute base
         */
        private static void addPlayerAttributeReward(Player player, UnshatteredAttributeValues attribute, double amount) {
            UnshatteredAttributeValues.modifyAttributeBaseValue(player, attribute, amount);
            player.sendSystemMessage(attributeGainMessage(attribute, amount));
        }

        public String getId() {
            return id;
        }

        @Override
        public @NonNull String getSerializedName() {
            return id;
        }

        public static Codec<Skill> CODEC = StringRepresentable.fromEnum(Skill::values);
    }

    public PlayerSkillsAttachment(float combatExp, float farmingExp, float fishingExp, float miningExp, float foragingExp, float magecraftExp) {
        skillExp[Skill.COMBAT.ordinal()] = combatExp;
        skillExp[Skill.FARMING.ordinal()] = farmingExp;
        skillExp[Skill.FISHING.ordinal()] = fishingExp;
        skillExp[Skill.MINING.ordinal()] = miningExp;
        skillExp[Skill.FORAGING.ordinal()] = foragingExp;
        skillExp[Skill.MAGECRAFT.ordinal()] = magecraftExp;
    }

    public float getExp(Skill skill) {
        return skillExp[skill.ordinal()];
    }

    public void addExp(Skill skill, float amount, Player player, Holder<Attribute> wisdomAttribute) {
        if (amount == 0.0f) return;

        int currentLevel = getLevel(getExp(skill));
        float expGainedAmount = amount * (float) (player.getAttributeValue(wisdomAttribute) / 100);
        skillExp[skill.ordinal()] += expGainedAmount;
        int levelDifference = getLevel(getExp(skill)) - currentLevel;
        for (int i = 0; i < levelDifference; i++) {
            StringBuilder borderBar = new StringBuilder();
            borderBar.repeat("▬", 60);
            player.sendSystemMessage(Component.literal(borderBar.toString()).withColor(0xFF00ADAB));
            // gonna start formatting these long append bits more like this, it gets really confusing otherwise
            if (currentLevel > 0) {
                player.sendSystemMessage(
                    // component.empty because i couldnt reset the chat formatting for some reason
                    Component.empty().append(Component.literal(" SKILL LEVEL UP ").withStyle(ChatFormatting.BOLD).withColor(0xFF00FFFF))
                    .append(Component.literal(Component.translatable(skill.getTranslationKey()).getString() + " ").withColor(0xFF00ADAB))
                    .append(Component.literal(UnshatteredUtils.convertTextToRomanNumeral(currentLevel)).withColor(0xFF555555))
                    .append(Component.literal("\uD83E\uDC46").withColor(0xFF555555))
                    .append(Component.literal(UnshatteredUtils.convertTextToRomanNumeral(currentLevel + 1)).withColor(0xFF00ADAB))
                );
            } else {
                player.sendSystemMessage(
                    Component.empty().append(Component.literal(" SKILL LEVEL UP ").withStyle(ChatFormatting.BOLD).withColor(0xFF00FFFF))
                    .append(Component.literal(Component.translatable(skill.getTranslationKey()).getString() + " ").withColor(0xFF00ADAB))
                    .append(Component.literal(UnshatteredUtils.convertTextToRomanNumeral(1)).withColor(0xFF00ADAB))
                );
            }
            player.sendSystemMessage(Component.empty());
            player.sendSystemMessage(Component.literal(" REWARDS").withColor(0xFF00FF24).withStyle(ChatFormatting.BOLD));

            for (LevelReward reward : skill.getRewards(currentLevel)) {
                if (!player.level().isClientSide()) {
                    UnshatteredAttributeValues.modifyAttributeBaseValue(player, reward.attribute(), reward.amount());
                    player.sendSystemMessage(reward.getComponent());
                }
            }

            int coins = skill.getCoinReward(currentLevel);
            player.sendSystemMessage(Component.literal("    " + String.format("%,d", coins) + " " + Component.translatable("misc.unshattered.coins").getString()).withColor(0xFFFFAA00));
            player.getData(UnshatteredAttachments.PLAYER_CURRENCY.get()).addCoins(coins);
            player.syncData(UnshatteredAttachments.PLAYER_CURRENCY.get());

            currentLevel++;
            player.sendSystemMessage(Component.literal(borderBar.toString()).withColor(0xFF00ADAB));
        }
        player.sendOverlayMessage((Component.literal("+" + expGainedAmount + " ").append(Component.translatable(skill.getTranslationKey()))).withColor(0xFF00AAAA).append(Component.literal(" (").withColor(0xFF7D7874).append(Component.literal(String.format("%.2f", getPercentageToNextLevel(getExp(skill)) * 100) + "%").withColor(0xFFFFAA00).append(Component.literal(")").withColor(0xFF7D7874)))));
        PacketDistributor.sendToPlayer((ServerPlayer) player, new ExpSoundEffectPacket());
    }

    /**
     * @param exp total exp for a skill
     */
    public int getLevel(float exp) {
        int level = 0;
        while (level < SKILL_LEVEL_TABLE.length && exp >= SKILL_LEVEL_TABLE[level]) {
            level++;
        }
        return level;
    }

    /**
     *
     * @param exp the total current exp for the skill
     * @return a percentage float for the next level
     */
    public float getPercentageToNextLevel(float exp) {
        int index = 0;
        while (index < SKILL_LEVEL_TABLE.length && exp >= SKILL_LEVEL_TABLE[index]) {
            index++;
        }
        if (index < SKILL_LEVEL_TABLE.length) {
            float prevLevelXP = (index == 0) ? 0 : SKILL_LEVEL_TABLE[index - 1];
            return (exp - prevLevelXP) / (SKILL_LEVEL_TABLE[index] - prevLevelXP);
        } else {
            // if the player is max level
            return 1.0f;
        }
    }

    public float getNextLevelExpRequirement(int currentLevel) {
        if (currentLevel == 0) {
            return SKILL_LEVEL_TABLE[0];
        } else {
            return currentLevel < SKILL_LEVEL_TABLE.length ? SKILL_LEVEL_TABLE[currentLevel] - SKILL_LEVEL_TABLE[currentLevel - 1] : SKILL_LEVEL_TABLE[SKILL_LEVEL_TABLE.length - 1];
        }
    }

    public float getCurrentLevelExp(Skill skill, int currentLevel) {
        float previousThreshold = currentLevel == 0 ? 0 : SKILL_LEVEL_TABLE[currentLevel - 1];
        return getExp(skill) - previousThreshold;
    }

    public boolean isMaxLevel(int level) {
        return level >= SKILL_LEVEL_TABLE.length;
    }

    private static Component attributeGainMessage(UnshatteredAttributeValues attribute, double amount) {
        return Component.literal("    " + ((amount == Math.floor(amount)) ? String.valueOf((long) amount) : String.valueOf(amount)) + (attribute.percentage ? "%" : "")).withColor(0xFF00FF24)
                .append(Component.literal(" " + attribute.symbol + " ").withColor(attribute.color)
                        .append(Component.translatable(attribute.getTranslationKey()).withColor(attribute.color)));
    }

    // both codecs are pretty weird because i found them in different places online
    // at some point ill figure out how to make them myself so they seem more normal
    public static final Codec<PlayerSkillsAttachment> CODEC = Codec.unboundedMap(Codec.STRING, Codec.FLOAT).xmap(map -> {
        PlayerSkillsAttachment obj = new PlayerSkillsAttachment(0, 0, 0, 0, 0, 0);
        for (Map.Entry<String, Float> entry : map.entrySet()) {
            Skill skill = Skill.valueOf(entry.getKey());
            obj.skillExp[skill.ordinal()] = entry.getValue();
        }
        return obj;
    }, obj -> {
        Map<String, Float> map = new HashMap<>();
        for (Skill skill : Skill.values()) {
            map.put(skill.name(), obj.getExp(skill));
        }
        return map;
    });

    // remember to properly sync this
    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerSkillsAttachment> STREAM_CODEC = StreamCodec.of((buf, obj) -> {
        buf.writeInt(Skill.values().length);
        for (Skill skill : Skill.values()) {
            buf.writeFloat(obj.getExp(skill));
        }
    }, buf -> {
        int size = buf.readInt();
        float[] values = new float[Skill.values().length];
        for (int i = 0; i < size && i < values.length; i++) {
            values[i] = buf.readFloat();
        }
        PlayerSkillsAttachment obj = new PlayerSkillsAttachment(0, 0, 0, 0, 0, 0);
        for (Skill skill : Skill.values()) {
            obj.skillExp[skill.ordinal()] = values[skill.ordinal()];
        }
        return obj;
    });
}