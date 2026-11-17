package io.github.moosyu.data.drops;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.moosyu.attributes.UnshatteredAttributeValue;
import io.github.moosyu.data.attachments.PlayerSkillsAttachment;
import io.github.moosyu.items.ItemRange;
import io.github.moosyu.items.UnshatteredRarity;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;

import javax.annotation.Nullable;

import java.util.Locale;

import static io.github.moosyu.data.drops.DropType.getDropType;

public record DropData(DropReward reward, float dropChance) {
    /**
     * items
     */
    public DropData(ItemRange itemRange, float dropChance) {
        this(new ItemReward(itemRange), dropChance);
    }

    /**
     * items with 100% drop chance
     */
    public DropData(ItemRange itemRange) {
        this(new ItemReward(itemRange), 1.0f);
    }

    /**
     * coins
     */
    public DropData(int min, int max, float dropChance) {
        this(new CoinReward(min, max), dropChance);
    }

    /**
     * coins with 100% drop chance
     */
    public DropData(int min, int max) {
        this(new CoinReward(min, max), 1.0f);
    }

    /**
     * skill experience
     */
    public DropData(PlayerSkillsAttachment.Skill skill, float experienceAmount, float dropChance) {
        this(new ExperienceReward(skill, experienceAmount), dropChance);
    }

    /**
     * skill experience with 100% drop chance
     */
    public DropData(PlayerSkillsAttachment.Skill skill, float experienceAmount) {
        this(new ExperienceReward(skill, experienceAmount), 1.0f);
    }

    public static final Codec<DropData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    DropReward.CODEC.fieldOf("reward").forGetter(DropData::reward),
                    Codec.FLOAT.optionalFieldOf("drop_chance", 1.0f).forGetter(DropData::dropChance)
            ).apply(instance, DropData::new)
    );

    /**
     * for basic situations
     */
    public boolean getNonGuaranteedDrop(boolean rolledAboveOccasional, ServerPlayer player, @Nullable UnshatteredAttributeValue fortuneType) {
        RandomSource random = player.getRandom();
        double fortuneValue = fortuneType == null ? 0 : player.getAttributeValue(fortuneType.holder);

        if (reward instanceof ItemReward itemReward) {
            if (dropChance >= 1.0) {
                itemReward.grant(player, random, fortuneValue);
                return rolledAboveOccasional;
            }

            DropType type = getDropType(fortuneType == UnshatteredAttributeValue.COMBAT_FORTUNE ? dropChance : dropChance * 50);

            float modifiedDropChance;
            if (fortuneType == null) {
                modifiedDropChance = dropChance;
            } else {
                modifiedDropChance = (float) (dropChance * (1 + (fortuneValue / 100)));
            }

            // so the player cant roll a bunch of super rare drops in a single go ever if they're really lucky
            if (random.nextFloat() < modifiedDropChance) {
                if (commonDrop()) {
                    reward.grant(player, random, fortuneValue);
                } else if (!rolledAboveOccasional) {
                    float rewardAmount = reward.grant(player, random, fortuneValue);
                    UnshatteredRarity itemRarity = UnshatteredUtils.getItemRarity(itemReward.itemRange().item().getDefaultInstance());

                    player.sendSystemMessage(Component.empty()
                            .append(Component.literal(Component.translatable("drop_type.message.unshattered." + type.key).getString().toUpperCase()).withColor(type.colour).withStyle(ChatFormatting.BOLD))
                            .append(Component.literal(" "))
                            .append(Component.translatable(itemReward.itemRange().item().getDescriptionId()).withColor(itemRarity.getColour()))
                            .append(fortuneValue > 0 ?
                                    Component.literal(" (+" + Math.round(fortuneValue) + fortuneType.symbol + " ")
                                    .append(Component.translatable(fortuneType.getTranslationKey()))
                                    .append(Component.literal(")"))
                                    .withColor(fortuneType.color) : Component.empty()
                            ).append(rewardAmount > 1 ? Component.literal(" x" + reward.formatAmount(rewardAmount)) : Component.empty()).withColor(UnshatteredUtils.DARK_GRAY)
                    );

                    return true;
                }
            }

            return rolledAboveOccasional;
        } else if (reward instanceof CoinReward coinReward) {
            if (dropChance >= 1.0) {
                coinReward.grant(player, random, fortuneValue);
                return rolledAboveOccasional;
            }

            DropType type = getDropType(fortuneType == UnshatteredAttributeValue.COMBAT_FORTUNE ? dropChance : dropChance * 50);

            float modifiedDropChance;
            if (fortuneType == null) {
                modifiedDropChance = dropChance;
            } else {
                modifiedDropChance = (float) (dropChance * (1 + (fortuneValue / 100)));
            }

            // so the player cant roll a bunch of super rare drops in a single go ever if they're really lucky
            if (random.nextFloat() < modifiedDropChance) {
                if (commonDrop()) {
                    reward.grant(player, random, fortuneValue);
                } else if (!rolledAboveOccasional) {
                    float rewardAmount = reward.grant(player, random, fortuneValue);

                    player.sendSystemMessage(Component.empty()
                            .append(Component.literal(Component.translatable("drop_type.message.unshattered." + type.key).getString().toUpperCase()).withStyle(ChatFormatting.BOLD).withColor(type.colour))
                            .append(Component.literal(" "))
                            .append(Component.literal(coinReward.formatAmount(rewardAmount)).withStyle(ChatFormatting.BOLD).withColor(UnshatteredUtils.GOLD))
                            .append(Component.literal(" "))
                            .append(Component.translatable("misc.unshattered.coins").withColor(UnshatteredUtils.GOLD))
                            .append(Component.literal(".").withColor(UnshatteredUtils.GOLD))
                    );

                    return true;
                }
            }

            return rolledAboveOccasional;
        } else if (reward instanceof ExperienceReward experienceReward) {
            if (dropChance >= 1.0) {
                experienceReward.grant(player, random, fortuneValue);
                return rolledAboveOccasional;
            }

            DropType type = getDropType(fortuneType == UnshatteredAttributeValue.COMBAT_FORTUNE ? dropChance : dropChance * 50);

            float modifiedDropChance;
            if (fortuneType == null) {
                modifiedDropChance = dropChance;
            } else {
                modifiedDropChance = (float) (dropChance * (1 + (fortuneValue / 100)));
            }

            // so the player cant roll a bunch of super rare drops in a single go ever if they're really lucky
            if (random.nextFloat() < modifiedDropChance) {
                if (commonDrop()) {
                    reward.grant(player, random, fortuneValue);
                } else if (!rolledAboveOccasional) {
                    float rewardAmount = reward.grant(player, random, fortuneValue);

                    player.sendSystemMessage(Component.empty()
                            .append(Component.literal(Component.translatable("drop_type.message.unshattered." + type.key).getString().toUpperCase(Locale.ROOT)).withStyle(ChatFormatting.BOLD).withColor(type.colour))
                            .append(Component.literal(" "))
                            .append(Component.literal(experienceReward.formatAmount(rewardAmount)).withStyle(ChatFormatting.BOLD).withColor(UnshatteredUtils.DARK_AQUA))
                            .append(Component.literal(" "))
                            .append(Component.translatable("misc.unshattered.experience").withColor(UnshatteredUtils.DARK_AQUA))
                            .append(Component.literal(".").withColor(UnshatteredUtils.DARK_AQUA))
                    );

                    return true;
                }
            }
        }

        return false;
    }

    private boolean commonDrop() {
        return !(dropChance < DropType.OCCASIONAL.minRate);
    }
}