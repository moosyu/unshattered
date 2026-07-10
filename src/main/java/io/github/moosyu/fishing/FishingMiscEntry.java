package io.github.moosyu.fishing;

import io.github.moosyu.attachments.PlayerCurrencyAttachment;
import io.github.moosyu.attachments.PlayerSkillsAttachment;
import io.github.moosyu.packets.ExpSoundEffectPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static io.github.moosyu.attachments.UnshatteredAttachments.PLAYER_CURRENCY;
import static io.github.moosyu.attachments.UnshatteredAttachments.PLAYER_SKILLS;

public record FishingMiscEntry(Consumer<Player> reward, Predicate<Player> condition) implements FishingEntry {
    public FishingMiscEntry(Consumer<Player> reward) {
        this(reward, _ -> true);
    }

    @Override
    public FishingRewardTypes type() {
        return FishingRewardTypes.ITEM;
    }

    public static FishingMiscEntry createCoinReward(int min, int max, String messageKey, int color, float expAmount) {
        return new FishingMiscEntry(player -> {
            PlayerCurrencyAttachment currencyAttachment = player.getData(PLAYER_CURRENCY.get());
            PlayerSkillsAttachment skillsAttachment = player.getData(PLAYER_SKILLS.get());
            int coins = ThreadLocalRandom.current().nextInt(min, max + 1);

            skillsAttachment.addExp(PlayerSkillsAttachment.Skill.FISHING, expAmount, player);
            player.syncData(PLAYER_SKILLS);
            player.sendSystemMessage(sendCoinMessage(messageKey, coins, color));
            currencyAttachment.addCoins(coins);
            player.syncData(PLAYER_CURRENCY);
        });
    }

    private static Component sendCoinMessage(String typeKey, int amount, int typeColor) {
        return Component.empty()
                .append(Component.translatable(typeKey).withColor(typeColor).withStyle(ChatFormatting.BOLD))
                .append(Component.literal(" "))
                .append(Component.translatable("skills.messages.unshattered.fishing.you_found").withColor(0xFF55FFFF))
                .append(Component.literal(" "))
                .append(Component.literal(String.format("%,d", amount) + " ").withColor(0xFFFFAA00))
                .append(Component.translatable("misc.unshattered.coins").withColor(0xFFFFAA00))
                .append(Component.literal("."));
    }
}
