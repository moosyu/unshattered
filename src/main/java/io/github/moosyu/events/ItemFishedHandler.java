package io.github.moosyu.events;

import io.github.moosyu.attachments.PlayerCollectionsAttachment;
import io.github.moosyu.attachments.PlayerSkillsAttachment;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.experience.EntityFishingExperience;
import io.github.moosyu.fishing.FishingItemEntry;
import io.github.moosyu.fishing.FishingMiscEntry;
import io.github.moosyu.fishing.FishingMobEntry;
import io.github.moosyu.fishing.tables.WaterEntries;
import io.github.moosyu.attachments.UnshatteredAttachments;
import io.github.moosyu.packets.ExpSoundEffectPacket;
import io.github.moosyu.util.AddItemToInventory;
import io.github.moosyu.util.FortuneCalculation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemFishedEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.attachments.UnshatteredAttachments.PLAYER_SKILLS;

@EventBusSubscriber(modid = MODID)
public class ItemFishedHandler {
    @SubscribeEvent
    public static void onItemFished(ItemFishedEvent event) {
        Player player = event.getEntity();
        Level level = player.level();
        if (level.isClientSide()) return;
        event.setCanceled(true);

        AttributeInstance fishingFortuneAttribute = player.getAttribute(UnshatteredAttributeValues.FISHING_FORTUNE.holder);
        PlayerSkillsAttachment skills = player.getData(UnshatteredAttachments.PLAYER_SKILLS.get());
        double totalWeight;

        if (fishingFortuneAttribute == null) {
            return;
        }

        final int SELECTED_TABLE_RANGE = ThreadLocalRandom.current().nextInt(200);
        final double FISHING_FORTUNE = fishingFortuneAttribute.getValue();

        if (SELECTED_TABLE_RANGE < 160) {
            Map<FishingItemEntry, Double> selectedMap = WaterEntries.filterEntries(WaterEntries.WATER_ITEM_WEIGHTS, player);
            totalWeight = WaterEntries.calculateTableWeight(selectedMap);
            double currentWeight = ThreadLocalRandom.current().nextDouble(0.0d, totalWeight);

            for (Map.Entry<FishingItemEntry, Double> entry : selectedMap.entrySet()) {
                currentWeight -= calculateAdjustedWeight(entry.getValue(), FISHING_FORTUNE);
                if (currentWeight <= 0) {
                    Item selectedItem = entry.getKey().item();
                    float expReward = selectedItem.components().getOrDefault(UnshatteredDataComponents.ITEM_EXP_REWARD.get(), 0.0f);
                    ItemStack itemRewards = new ItemStack(selectedItem, FortuneCalculation.getItemsCount(FISHING_FORTUNE, 1));

                    AddItemToInventory.addItemToInventory(player, itemRewards);

                    if (expReward > 0.0f) {
                        skills.addExp(PlayerSkillsAttachment.Skill.FISHING, expReward, player);
                        player.syncData(PLAYER_SKILLS);
                    }

                    break;
                }
            }
        } else if (SELECTED_TABLE_RANGE < 194) {
            Map<FishingMobEntry, Double> selectedMap = WaterEntries.filterEntries(WaterEntries.WATER_MOB_WEIGHTS, player);
            totalWeight = WaterEntries.calculateTableWeight(selectedMap);
            double currentWeight = ThreadLocalRandom.current().nextDouble(0.0d, totalWeight);

            for (Map.Entry<FishingMobEntry, Double> entry : selectedMap.entrySet()) {
                currentWeight -= calculateAdjustedWeight(entry.getValue(), FISHING_FORTUNE);
                if (currentWeight <= 0) {
                    EntityType<?> entityType = entry.getKey().entity();
                    Entity entity = entityType.create(level, EntitySpawnReason.TRIGGERED);
                    if (entity != null) {
                        FishingHook hook = event.getHookEntity();

                        entity.setPos(hook.position());
                        entity.setDeltaMovement(player.position().subtract(entity.position()).normalize().scale(3.5D));
                        player.sendSystemMessage(Component.translatable("fishing.messages." + entityType.getDescriptionId()).withColor(0xFF55FF55));
                        level.addFreshEntity(entity);

                        skills.addExp(PlayerSkillsAttachment.Skill.FISHING, EntityFishingExperience.getExp(entityType), player);
                    }
                }
            }
        } else {
            Map<FishingMiscEntry, Double> selectedMap = WaterEntries.filterEntries(WaterEntries.WATER_MISC_WEIGHTS, player);
            totalWeight = WaterEntries.calculateTableWeight(selectedMap);
            double currentWeight = ThreadLocalRandom.current().nextDouble(0.0d, totalWeight);

            for (Map.Entry<FishingMiscEntry, Double> entry : selectedMap.entrySet()) {
                currentWeight -= calculateAdjustedWeight(entry.getValue(), FISHING_FORTUNE);
                if (currentWeight <= 0) {
                    // this (should) deal with exp and stuff itself
                    entry.getKey().reward().accept(player);
                    break;
                }
            }
        }
    }

    private static double calculateAdjustedWeight(double weight, double fishingFortune) {
        return Math.pow(weight, 1.0 - ((fishingFortune / UnshatteredAttributeValues.FISHING_FORTUNE.max) * 0.5));
    }
}
