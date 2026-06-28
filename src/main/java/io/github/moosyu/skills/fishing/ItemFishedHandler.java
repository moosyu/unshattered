package io.github.moosyu.skills.fishing;

import io.github.moosyu.Unshattered;
import io.github.moosyu.attachments.PlayerCurrencyAttachment;
import io.github.moosyu.attachments.PlayerSkillsAttachment;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.packets.ExpSoundEffectPacket;
import io.github.moosyu.attachments.UnshatteredAttachments;
import io.github.moosyu.util.FortuneCalculationHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemFishedEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.attachments.UnshatteredAttachments.PLAYER_CURRENCY;
import static io.github.moosyu.attachments.UnshatteredAttachments.PLAYER_SKILLS;

@EventBusSubscriber(modid = MODID)
public class ItemFishedHandler {
    @SubscribeEvent
    public static void onItemFished(ItemFishedEvent event) {
        Player player = event.getEntity();
        Level level = player.level();
        if (level.isClientSide()) return;
        event.setCanceled(true);

        Random rand = new Random();
        PlayerSkillsAttachment skills = player.getData(UnshatteredAttachments.PLAYER_SKILLS.get());
        AttributeInstance fishingFortuneAttribute = player.getAttribute(UnshatteredAttributeValues.FISHING_FORTUNE.holder);
        List<FishingResultsEntry> filteredFishingOptions = new ArrayList<>();
        Identifier selectedResultsTable;
        FishingHook hook = event.getHookEntity();
        double totalWeight = 0;
        if (fishingFortuneAttribute == null) {
            return;
        }

        if (rand.nextInt(4) > 0) {
            selectedResultsTable = Identifier.fromNamespaceAndPath(MODID, "base_water_items");
        } else {
            selectedResultsTable = Identifier.fromNamespaceAndPath(MODID, "base_water_sea_creatures");
        }

        for (FishingResultsEntry entry : FishingManager.getEntries(selectedResultsTable)) {
            if (entry.levelRequirement() <= skills.getLevel(skills.getExp(PlayerSkillsAttachment.Skill.FISHING)) && entry.conditions().stream().allMatch(condition -> condition.test(new FishingContext((ServerLevel) level, player, hook)))) {
                filteredFishingOptions.add(entry);
                totalWeight += calculateAdjustedWeight(entry.weight(), fishingFortuneAttribute.getValue());
            }
        }

        double weightSelected = rand.nextDouble(0.0d, totalWeight);
        FishingResultsEntry selectedEntry = null;

        for (FishingResultsEntry entry : filteredFishingOptions) {
            weightSelected -= calculateAdjustedWeight(entry.weight(), fishingFortuneAttribute.getValue());
            if (weightSelected <= 0) {
                selectedEntry = entry;
                break;
            }
        }

        if (selectedEntry == null) {
            Unshattered.LOGGER.error("something went wrong! no entry has been selected!");
            return;
        }

        switch (selectedEntry.type()) {
            case "ITEM" ->
                    player.getInventory().add(new ItemStack(BuiltInRegistries.ITEM.getValue(Identifier.parse(selectedEntry.id())), FortuneCalculationHelper.getItemsCount(fishingFortuneAttribute.getValue(), 1)));
            case "COIN" -> {
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
            }
            case "MOB" -> {
                Identifier id = Identifier.parse(selectedEntry.id());
                EntityType<?> entityType = BuiltInRegistries.ENTITY_TYPE.getValue(id);
                Entity entity = entityType.create(level, EntitySpawnReason.TRIGGERED);

                if (entity != null) {
                    entity.setPos(hook.position());
                    entity.setDeltaMovement(player.position().subtract(entity.position()).normalize().scale(3.5D));
                    player.sendSystemMessage(Component.translatable("fishing.messages." + id.getNamespace() + "." + id.getPath()).withColor(0xFF55FF55));
                    level.addFreshEntity(entity);
                }
            }
            case null, default -> Unshattered.LOGGER.error("type for: {} is messed up", selectedEntry.id());
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

    private static double calculateAdjustedWeight(double weight, double fishingFortune) {
        return Math.pow(weight, 1.0 - ((fishingFortune / UnshatteredAttributeValues.FISHING_FORTUNE.max) * 0.5));
    }
}
