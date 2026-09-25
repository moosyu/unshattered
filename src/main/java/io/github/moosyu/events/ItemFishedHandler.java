package io.github.moosyu.events;

import io.github.moosyu.data.attachments.PlayerSkillsAttachment;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.UnshatteredDataMaps;
import io.github.moosyu.data.fishing.FishingItemEntry;
import io.github.moosyu.data.fishing.FishingMiscEntry;
import io.github.moosyu.data.fishing.FishingMobEntry;
import io.github.moosyu.data.fishing.tables.WaterEntries;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemFishedEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.data.attachments.UnshatteredAttachments.PLAYER_SKILLS;

@EventBusSubscriber(modid = MODID)
public class ItemFishedHandler {
    public record LaunchData(Vec3 start, Vec3 target, double peakY, long startTick, int durationTicks) {}
    public static final Map<Entity, LaunchData> ACTIVE_LAUNCHES = new HashMap<>();

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
                    float expReward = Objects.requireNonNullElse(BuiltInRegistries.ITEM.wrapAsHolder(selectedItem).getData(UnshatteredDataMaps.FISHABLE_ITEMS_EXP_DATA), 0.0f);
                    ItemStack itemRewards = new ItemStack(selectedItem, UnshatteredUtils.getItemsCount(FISHING_FORTUNE, 1));

                    UnshatteredUtils.givePlayerHarvestedItemStack(player, itemRewards);

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
                        Vec3 startPos = event.getHookEntity().position();
                        Vec3 targetPos = player.position();
                        double horizontalDist = Math.sqrt(startPos.distanceToSqr(targetPos.x, startPos.y, targetPos.z));

                        // needs to be self and passengers so i can add the hog rider fucker later down the line
                        entity.getSelfAndPassengers().forEach(e -> e.setPos(startPos.x, startPos.y, startPos.z));

                        if (entity instanceof Mob mob) {
                            // some mobs get weird and it messes with the movement
                            mob.setNoAi(true);
                        }

                        ACTIVE_LAUNCHES.put(entity, new LaunchData(startPos, targetPos, Math.max(startPos.y, targetPos.y) + Math.max(1.0, horizontalDist * 0.3), level.getGameTime(), Mth.clamp((int) (horizontalDist / 2), 12, 100)));

                        player.sendSystemMessage(Component.translatable("fishing.messages." + entityType.getDescriptionId()).withColor(0xFF55FF55));

                        if (level instanceof ServerLevel serverLevel) {
                            serverLevel.tryAddFreshEntityWithPassengers(entity);
                        }

                        skills.addExp(PlayerSkillsAttachment.Skill.FISHING, Objects.requireNonNullElse(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(entityType).getData(UnshatteredDataMaps.FISHABLE_MOBS_EXP_DATA), 0.0f), player);
                    }

                    break;
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
