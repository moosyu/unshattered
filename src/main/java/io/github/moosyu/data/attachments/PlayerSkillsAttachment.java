package io.github.moosyu.data.attachments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.packets.ExpSoundEffectPacket;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public final class PlayerSkillsAttachment {
    private static final int[] SKILL_LEVEL_TABLE = {50, 175, 375, 675, 1175, 1925, 2925, 4425, 6425, 9925};
    private static final int[] COINS_LEVEL_TABLE = {100, 250, 500, 750, 1000, 2000, 3000, 4000, 5000, 7500, 10000, 15000, 20000, 25000, 30000, 40000, 50000, 65000, 80000, 100000, 125000, 150000, 175000, 200000, 225000, 250000, 275000, 300000, 325000, 350000, 375000, 400000, 425000, 450000, 475000, 500000, 550000, 600000, 650000, 700000, 750000, 800000, 850000, 900000, 1000000, 1000000, 1000000, 1000000, 1000000, 1000000};
    private final float[] skillExp = new float[Skill.values().length];

    public enum Skill {
        COMBAT("combat", (player, _) -> {
            // i know normal sb gives you a flat damage multiplier but i feel like thats a little strange so ill give people an actual stat
            addPlayerAttributeReward(player, UnshatteredAttributeValues.CRITICAL_DAMAGE, 10.0d);
            addPlayerAttributeReward(player, UnshatteredAttributeValues.CRITICAL_CHANCE, 0.5d);

        }),
        FARMING("farming", (player, level) -> {
            double healthAmount;
            if (level < 15) {
                healthAmount = 1.0d;
            } else if (level < 20) {
                healthAmount = 2.0d;
            } else if (level < 25) {
                healthAmount = 3.0d;
            } else {
                healthAmount = 4.0d;
            }
            addPlayerAttributeReward(player, UnshatteredAttributeValues.HEALTH, healthAmount);
            addPlayerAttributeReward(player, UnshatteredAttributeValues.FARMING_FORTUNE, 2.0d);
        }),
        FISHING("fishing", (player, _) -> {
            addPlayerAttributeReward(player, UnshatteredAttributeValues.FISHING_SPEED, 1.0d);
            addPlayerAttributeReward(player, UnshatteredAttributeValues.FISHING_FORTUNE, 0.1d);

        }),
        MINING("mining", (player, level) -> {
            double defenseAmount;
            if (level < 15) {
                defenseAmount = 1.0d;
            } else {
                defenseAmount = 2.0d;
            }
            addPlayerAttributeReward(player, UnshatteredAttributeValues.DEFENSE, defenseAmount);
            addPlayerAttributeReward(player, UnshatteredAttributeValues.MINING_FORTUNE, 2.0d);

        }),
        FORAGING("foraging", (player, level) -> {
            double strengthAmount;
            if (level < 15) {
                strengthAmount = 1.0d;
            } else {
                strengthAmount = 2.0d;
            }
            addPlayerAttributeReward(player, UnshatteredAttributeValues.STRENGTH, strengthAmount);
            addPlayerAttributeReward(player, UnshatteredAttributeValues.FARMING_FORTUNE, 2.0d);

        }),
        MAGECRAFT("magecraft", (player, level) -> {
            addPlayerAttributeReward(player, UnshatteredAttributeValues.MANA, 2.0d);
            addPlayerAttributeReward(player, UnshatteredAttributeValues.MANA_REGEN, 1.5d);

        }),
        CARPENTRY("carpentry", (player, level) -> {
            addPlayerAttributeReward(player, UnshatteredAttributeValues.HEALTH, 1.0d);
        });

        private final String id;
        /**
         * integer is the level
         */
        private final BiConsumer<Player, Integer> levelUpReward;

        Skill(String id, BiConsumer<Player, Integer> levelUpReward) {
            this.id = id;
            this.levelUpReward = levelUpReward;
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

        private static final Map<String, Skill> BY_ID = Arrays.stream(values()).collect(Collectors.toMap(Skill::getId, s -> s));

        public static Optional<Skill> byId(String id) {
            return Optional.ofNullable(BY_ID.get(id));
        }

        public static final Codec<Skill> CODEC = Codec.STRING.comapFlatMap(id -> byId(id).map(DataResult::success).orElseGet(() -> DataResult.error(() -> "broken id: " + id)), Skill::getId);
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

    public void addExp(Skill skill, float amount, Player player) {
        int currentLevel = getLevel(getExp(skill));
        skillExp[skill.ordinal()] += amount;
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
            player.sendSystemMessage(Component.literal(""));
            player.sendSystemMessage(Component.literal(" REWARDS").withColor(0xFF00FF24).withStyle(ChatFormatting.BOLD));
            if (!player.level().isClientSide()) {
                skill.levelUpReward.accept(player, currentLevel);
            }
            player.sendSystemMessage(Component.literal("    " + String.format("%,d", COINS_LEVEL_TABLE[currentLevel]) + " " + Component.translatable("misc.unshattered.coins").getString()).withColor(0xFFFFAA00));
            player.getData(UnshatteredAttachments.PLAYER_CURRENCY.get()).addCoins(COINS_LEVEL_TABLE[currentLevel]);
            player.syncData(UnshatteredAttachments.PLAYER_CURRENCY.get());
            currentLevel++;
            player.sendSystemMessage(Component.literal(borderBar.toString()).withColor(0xFF00ADAB));
        }
        player.sendOverlayMessage((Component.literal("+" + amount + " ").append(Component.translatable(skill.getTranslationKey()))).withColor(0xFF00AAAA).append(Component.literal(" (").withColor(0xFF7D7874).append(Component.literal(String.format("%.2f", getPercentageToNextLevel(getExp(skill)) * 100) + "%").withColor(0xFFFFAA00).append(Component.literal(")").withColor(0xFF7D7874)))));
        PacketDistributor.sendToPlayer((ServerPlayer) player, new ExpSoundEffectPacket());
    }

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
            return 1;
        }
    }

    private static Component attributeGainMessage(UnshatteredAttributeValues attribute, double amount) {
        return Component.literal("    " + ((amount == Math.floor(amount)) ? String.valueOf((long) amount) : String.valueOf(amount)) + (attribute.percentage ? "%" : "")).withColor(0xFF00FF24)
                .append(Component.literal(" " + attribute.symbol + " ").withColor(attribute.color)
                        .append(Component.translatable(attribute.getTranslationKey()).withColor(attribute.color)));
    }

    // both codecs are pretty weird because i found them in different places online
    // at some point ill figure out how to make them myself so they seem more normal
    public static final Codec<PlayerSkillsAttachment> RECORD_CODEC = Codec.unboundedMap(Codec.STRING, Codec.FLOAT).xmap(map -> {
        PlayerSkillsAttachment obj = new PlayerSkillsAttachment(0, 0, 0, 0, 0, 0);
        for (var entry : map.entrySet()) {
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