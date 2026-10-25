package io.github.moosyu.data.fishing.rewards;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.attachments.PlayerCurrencyAttachment;
import io.github.moosyu.data.attachments.PlayerSkillsAttachment;
import io.github.moosyu.data.fishing.FishingCondition;
import io.github.moosyu.data.fishing.TriggerMiscReward;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;
import java.util.Optional;

import static io.github.moosyu.data.attachments.UnshatteredAttachments.PLAYER_CURRENCY;
import static io.github.moosyu.data.attachments.UnshatteredAttachments.PLAYER_SKILLS;

public record CoinReward(int min, int max, String messageKey, int colour, float expAmount, Optional<List<FishingCondition>> conditions, double weight, Optional<Integer> fishingLevelRequirement) implements TriggerMiscReward {
    public static final MapCodec<CoinReward> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("min").forGetter(CoinReward::min),
            Codec.INT.fieldOf("max").forGetter(CoinReward::max),
            Codec.STRING.fieldOf("message_key").forGetter(CoinReward::messageKey),
            Codec.INT.fieldOf("colour").forGetter(CoinReward::colour),
            Codec.FLOAT.fieldOf("exp_amount").forGetter(CoinReward::expAmount),
            FishingCondition.CODEC.listOf().optionalFieldOf("conditions").forGetter(CoinReward::conditions),
            Codec.DOUBLE.fieldOf("weight").forGetter(CoinReward::weight),
            Codec.INT.optionalFieldOf("level_requirement").forGetter(CoinReward::fishingLevelRequirement)
    ).apply(instance, CoinReward::new));

    @Override
    public void trigger(ServerPlayer player) {
        PlayerCurrencyAttachment currencyAttachment = player.getData(PLAYER_CURRENCY.get());
        PlayerSkillsAttachment skillsAttachment = player.getData(PLAYER_SKILLS.get());
        int coins = player.getRandom().nextIntBetweenInclusive(min, max);

        skillsAttachment.addExp(PlayerSkillsAttachment.Skill.FISHING, expAmount, player, UnshatteredAttributeValues.FISHING_WISDOM.holder);
        player.syncData(PLAYER_SKILLS);

        player.sendSystemMessage(Component.empty()
                .append(Component.translatable(messageKey).withColor(colour).withStyle(ChatFormatting.BOLD))
                .append(Component.literal(" "))
                .append(Component.translatable("skills.messages.unshattered.fishing.you_found").withColor(0xFF55FFFF))
                .append(Component.literal(" "))
                .append(Component.literal(String.format("%,d", coins) + " ").withColor(0xFFFFAA00))
                .append(Component.translatable("misc.unshattered.coins").withColor(0xFFFFAA00))
                .append(Component.literal("."))
        );

        currencyAttachment.addCoins(coins);
        player.syncData(PLAYER_CURRENCY);
    }

    @Override
    public MapCodec<? extends TriggerMiscReward> codec() {
        return CODEC;
    }
}
