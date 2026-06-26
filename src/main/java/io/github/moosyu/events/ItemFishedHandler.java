package io.github.moosyu.events;

import io.github.moosyu.Unshattered;
import io.github.moosyu.attachments.PlayerCurrencyAttachment;
import io.github.moosyu.attachments.PlayerSkillsAttachment;
import io.github.moosyu.packets.ExpSoundEffectPacket;
import io.github.moosyu.attachments.UnshatteredAttachments;
import io.github.moosyu.skills.fishing.FishingManager;
import io.github.moosyu.skills.fishing.FishingResultsEntry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemFishedEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.attachments.UnshatteredAttachments.PLAYER_CURRENCY;
import static io.github.moosyu.attachments.UnshatteredAttachments.PLAYER_SKILLS;

@EventBusSubscriber(modid = MODID)
public class ItemFishedHandler {
    @SubscribeEvent
    public static void onItemFished(ItemFishedEvent event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;
        event.setCanceled(true);

        Random rand = new Random();
        PlayerSkillsAttachment skills = player.getData(UnshatteredAttachments.PLAYER_SKILLS.get());
        List<FishingResultsEntry> filteredFishingOptions = new ArrayList<>();
        int totalWeight = 0;
        for (FishingResultsEntry entry : FishingManager.getEntries(Identifier.fromNamespaceAndPath(MODID, "default"))) {
            if (entry.levelRequirement() <= skills.getLevel(skills.getExp(PlayerSkillsAttachment.Skill.FISHING))) {
                filteredFishingOptions.add(entry);
                totalWeight += entry.weight();
            }
        }

        int weightSelected = rand.nextInt(0, totalWeight);
        FishingResultsEntry selectedEntry = null;
        for (FishingResultsEntry entry : filteredFishingOptions) {
            weightSelected -= entry.weight();
            if (weightSelected <= 0) {
                selectedEntry = entry;
                break;
            }
        }
        if (selectedEntry == null) {
            Unshattered.LOGGER.error("something went wrong with the weighting!! (nothing was selected)");
        }

        if (Objects.equals(selectedEntry.type(), "ITEM")) {
            player.getInventory().add(new ItemStack(BuiltInRegistries.ITEM.getValue(Identifier.parse(selectedEntry.id()))));
        } else if (Objects.equals(selectedEntry.type(), "COIN")) {
            PlayerCurrencyAttachment currencyAttachment = player.getData(UnshatteredAttachments.PLAYER_CURRENCY.get());
            int coinAmount = 0;
            switch (selectedEntry.id()) {
                case "good_coins" -> {
                    coinAmount = rand.nextInt(25000, 50001);
                    player.sendSystemMessage(sendCoinMessage("skills.messages.unshattered.fishing.good_catch", coinAmount, 0xFF810AF3));
                }
                case "great_coins" -> {
                    coinAmount = rand.nextInt(100000, 250001);
                    player.sendSystemMessage(sendCoinMessage("skills.messages.unshattered.fishing.great_catch", coinAmount, 0xFFFFAA00));
                }
                case "outstanding_coins" -> {
                    coinAmount = rand.nextInt(500000, 1000001);
                    player.sendSystemMessage(sendCoinMessage("skills.messages.unshattered.fishing.outstanding_catch", coinAmount, 0xFF55FF));
                }
            }
            currencyAttachment.addCoins(coinAmount);
            player.syncData(PLAYER_CURRENCY);
        } else {
            Unshattered.LOGGER.error("type for: {} is messed up", selectedEntry.id());
        }
        skills.addExp(PlayerSkillsAttachment.Skill.FISHING, selectedEntry.exp(), player);
        player.syncData(PLAYER_SKILLS);
        PacketDistributor.sendToPlayer((ServerPlayer) player, new ExpSoundEffectPacket());
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
