package io.github.moosyu.attachments;

import com.mojang.serialization.Codec;
import io.github.moosyu.util.TextHelpers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;

public class PlayerSkillsAttachment {
    private static final int[] SKILL_LEVEL_TABLE = {50, 175, 375, 675, 1175, 1925, 2925, 4425, 6425, 9925};
    private final float[] skillExp = new float[Skill.values().length];

    public enum Skill {
        COMBAT("skills.name.unshattered.combat", Items.STONE_SWORD),
        FARMING("skills.name.unshattered.farming", Items.GOLDEN_HOE),
        FISHING("skills.name.unshattered.fishing", Items.FISHING_ROD),
        MINING("skills.name.unshattered.mining", Items.STONE_PICKAXE),
        FORAGING("skills.name.unshattered.foraging", Items.OAK_SAPLING),
        MAGECRAFT("skills.name.unshattered.magecraft", Items.BOOK);

        private final String key;
        private final Item icon;

        Skill(String key, Item icon) {
            this.key = key;
            this.icon = icon;
        }

        public String getName() {
            return Component.translatable(key).getString();
        }

        public Item getIcon() {
            return icon;
        }
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
                    .append(Component.literal(Component.translatable(skill.key).getString() + " ").withColor(0xFF00ADAB))
                    .append(Component.literal(TextHelpers.convertTextToRomanNumeral(currentLevel)).withColor(0xFF555555))
                    .append(Component.literal("\uD83E\uDC46").withColor(0xFF555555))
                    .append(Component.literal(TextHelpers.convertTextToRomanNumeral(currentLevel + 1)).withColor(0xFF00ADAB))
                );
            } else {
                player.sendSystemMessage(
                    Component.empty().append(Component.literal(" SKILL LEVEL UP ").withStyle(ChatFormatting.BOLD).withColor(0xFF00FFFF))
                    .append(Component.literal(Component.translatable(skill.key).getString() + " ").withColor(0xFF00ADAB))
                    .append(Component.literal(TextHelpers.convertTextToRomanNumeral(1)).withColor(0xFF00ADAB))
                );
            }
            currentLevel++;

            player.sendSystemMessage(Component.literal(""));
            player.sendSystemMessage(Component.literal(" REWARDS").withColor(0xFF00FF24).withStyle(ChatFormatting.BOLD));
            player.sendSystemMessage(Component.literal(borderBar.toString()).withColor(0xFF00ADAB));
        }
        player.sendOverlayMessage((Component.literal("+" + amount + " ").append(Component.translatable(skill.key))).withColor(0xFF00AAAA).append(Component.literal(" (").withColor(0xFF7D7874).append(Component.literal(String.format("%.2f", getPercentageToNextLevel(getExp(skill)) * 100) + "%").withColor(0xFFFFAA00).append(Component.literal(")").withColor(0xFF7D7874)))));
    }

    public int getLevel(float exp) {
        int level = 0;
        while (level < SKILL_LEVEL_TABLE.length && exp >= SKILL_LEVEL_TABLE[level]) {
            level++;
        }
        return level;
    }

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