package io.github.moosyu.attachments;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;

import java.util.HashMap;
import java.util.Map;

public class PlayerSkillsAttachment {
    private static final int[] SKILL_LEVEL_TABLE = {50, 175, 375, 675, 1175, 1925, 2925, 4425, 6425, 9925};
    private final float[] skillExp = new float[Skill.values().length];
    private final float playerExp;

    // todo: ideally these should be using like lang files for names but i dont really intend on translating this mod so i dont mind rn
    public enum Skill {
        COMBAT("skills.name.unshattered.combat", Items.STONE_SWORD),
        FARMING("skills.name.unshattered.farming", Items.GOLDEN_HOE),
        FISHING("skills.name.unshattered.fishing", Items.FISHING_ROD),
        MINING("skills.name.unshattered.mining", Items.STONE_PICKAXE),
        FORAGING("skills.name.unshattered.foraging", Items.OAK_SAPLING);

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

    public PlayerSkillsAttachment(float combatExp, float farmingExp, float fishingExp, float miningExp, float foragingExp, float playerExp) {
        skillExp[Skill.COMBAT.ordinal()] = combatExp;
        skillExp[Skill.FARMING.ordinal()] = farmingExp;
        skillExp[Skill.FISHING.ordinal()] = fishingExp;
        skillExp[Skill.MINING.ordinal()] = miningExp;
        skillExp[Skill.FORAGING.ordinal()] = foragingExp;
        this.playerExp = playerExp;
    }

    public float getExp(Skill skill) {
        return skillExp[skill.ordinal()];
    }

    public void addExp(Skill skill, float amount) {
        skillExp[skill.ordinal()] += amount;
    }

    public int getLevel(float exp) {
        int level = 0;
        while (level < SKILL_LEVEL_TABLE.length && exp >= SKILL_LEVEL_TABLE[level]) {
            level++;
        }
        return level;
    }

    public float getPercentageToLevel(float exp) {
        int index = 0;
        while (index < SKILL_LEVEL_TABLE.length && exp >= SKILL_LEVEL_TABLE[index]) {
            index++;
        }
        float prevLevelXP = (index == 0) ? 0 : SKILL_LEVEL_TABLE[index - 1];
        return (exp - prevLevelXP) / (SKILL_LEVEL_TABLE[index] - prevLevelXP);
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
            },
            obj -> {
                Map<String, Float> map = new HashMap<>();
                for (Skill skill : Skill.values()) {
                    map.put(skill.name(), obj.getExp(skill));
                }
                return map;
            }
    );

    // remember to properly sync this
    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerSkillsAttachment> STREAM_CODEC = StreamCodec.of((buf, obj) -> {
                for (Skill skill : Skill.values()) {
                    buf.writeFloat(obj.getExp(skill));
                }
                buf.writeFloat(obj.playerExp);
            },
            buf -> {
                float combat = buf.readFloat();
                float farming = buf.readFloat();
                float fishing = buf.readFloat();
                float mining = buf.readFloat();
                float foraging = buf.readFloat();
                float playerExp = buf.readFloat();
                return new PlayerSkillsAttachment(combat, farming, fishing, mining, foraging, playerExp);
            }
    );
}