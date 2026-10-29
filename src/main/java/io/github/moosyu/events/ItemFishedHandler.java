package io.github.moosyu.events;

import io.github.moosyu.Unshattered;
import io.github.moosyu.abilities.*;
import io.github.moosyu.data.attachments.PlayerSkillsAttachment;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.UnshatteredDataMaps;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.fishing.FishingWeightEntry;
import io.github.moosyu.data.fishing.TriggerMiscReward;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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

import java.util.*;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.data.attachments.UnshatteredAttachments.PLAYER_SKILLS;

@EventBusSubscriber(modid = MODID)
public class ItemFishedHandler {
    public record LaunchData(Vec3 start, Vec3 target, double peakY, long startTick, int durationTicks) {}
    public static final Map<Entity, LaunchData> ACTIVE_LAUNCHES = new HashMap<>();

    @SubscribeEvent
    public static void onItemFished(ItemFishedEvent event) {
        final Player PLAYER = event.getEntity();
        final Level LEVEL = PLAYER.level();
        if (LEVEL.isClientSide()) {
            return;
        }

        event.setCanceled(true);

        final AbilityContext CONTEXT = new AbilityContext().add(AbilityContextKey.PLAYER, (ServerPlayer) PLAYER);
        final List<PassiveAbilityItem> TRIGGERED_ITEMS = new ArrayList<>();
        for (ItemStack item : PLAYER.getData(UnshatteredAttachments.PLAYER_ABILITIES).getStoredNonOngoingItems()) {
            if (!(item.getItem() instanceof final PassiveAbilityItem PASSIVE_ABILITY_ITEM)
                    || !PASSIVE_ABILITY_ITEM.triggerTypes().contains(AbilityTriggerType.PLAYER_FISH)
                    || !PASSIVE_ABILITY_ITEM.abilityConditionsMet(CONTEXT)
            ) {
                continue;
            }

            PASSIVE_ABILITY_ITEM.onAbilityTriggered(CONTEXT);
            TRIGGERED_ITEMS.add(PASSIVE_ABILITY_ITEM);

            final Optional<?> RESULT = PASSIVE_ABILITY_ITEM.triggerResult();
            if (RESULT.isPresent() && RESULT.get() == AbilityTriggerResult.CANCEL_EVENT) {
                TRIGGERED_ITEMS.forEach(triggeredItem -> triggeredItem.onAbilityFinished(CONTEXT));
                return;
            }
        }

        final AttributeInstance FISHING_FORTUNE_ATTRIBUTE = PLAYER.getAttribute(UnshatteredAttributeValues.FISHING_FORTUNE.holder);
        if (FISHING_FORTUNE_ATTRIBUTE == null) {
            return;
        }

        final PlayerSkillsAttachment SKILLS = PLAYER.getData(UnshatteredAttachments.PLAYER_SKILLS.get());
        final int SELECTED_TABLE_RANGE = LEVEL.getRandom().nextInt(200);
        final double FISHING_FORTUNE = FISHING_FORTUNE_ATTRIBUTE.getValue();
        double totalWeight;

        if (SELECTED_TABLE_RANGE < 160) {
            final Map<ResourceKey<Item>, FishingWeightEntry> SELECTED_MAP = UnshatteredUtils.filterFishingEntries(BuiltInRegistries.ITEM.getDataMap(UnshatteredDataMaps.FISHING_ITEM_WEIGHT_DATA), (ServerPlayer) PLAYER, event.getHookEntity().blockPosition());
            totalWeight = UnshatteredUtils.calculateTableWeight(SELECTED_MAP);
            double currentWeight = LEVEL.getRandom().nextDouble() * totalWeight;

            for (Map.Entry<ResourceKey<Item>, FishingWeightEntry> entry : SELECTED_MAP.entrySet()) {
                currentWeight -= calculateAdjustedWeight(entry.getValue().weight(), FISHING_FORTUNE);
                if (currentWeight <= 0) {
                    final Item SELECTED_ITEM = BuiltInRegistries.ITEM.getValue(entry.getKey());
                    if (SELECTED_ITEM == null) {
                        Unshattered.LOGGER.error("{} is null!!", entry.getKey().toString());
                        break;
                    }

                    UnshatteredUtils.addItemStackToInventory(PLAYER, new ItemStack(SELECTED_ITEM, UnshatteredUtils.getItemsCount(FISHING_FORTUNE, 1)));

                    final float EXP_REWARD = Objects.requireNonNullElse(BuiltInRegistries.ITEM.wrapAsHolder(SELECTED_ITEM).getData(UnshatteredDataMaps.FISHABLE_ITEMS_EXP_DATA), 0.0f);
                    if (EXP_REWARD > 0.0f) {
                        SKILLS.addExp(PlayerSkillsAttachment.Skill.FISHING, EXP_REWARD, PLAYER, UnshatteredAttributeValues.FISHING_WISDOM.holder);
                        PLAYER.syncData(PLAYER_SKILLS);
                    }

                    break;
                }
            }
        } else if (SELECTED_TABLE_RANGE < 194) {
            final Map<ResourceKey<EntityType<?>>, FishingWeightEntry> SELECTED_MAP = UnshatteredUtils.filterFishingEntries(BuiltInRegistries.ENTITY_TYPE.getDataMap(UnshatteredDataMaps.FISHING_MOB_WEIGHT_DATA), (ServerPlayer) PLAYER, event.getHookEntity().blockPosition());
            totalWeight = UnshatteredUtils.calculateTableWeight(SELECTED_MAP);
            double currentWeight = LEVEL.getRandom().nextDouble() * totalWeight;

            for (Map.Entry<ResourceKey<EntityType<?>>, FishingWeightEntry> entry : SELECTED_MAP.entrySet()) {
                currentWeight -= calculateAdjustedWeight(entry.getValue().weight(), FISHING_FORTUNE);
                if (currentWeight <= 0) {
                    final EntityType<?> ENTITY_TYPE = BuiltInRegistries.ENTITY_TYPE.getValue(entry.getKey());
                    if (ENTITY_TYPE == null) {
                        Unshattered.LOGGER.error("{} is null", entry.getKey().toString());
                        break;
                    }

                    final Entity FISHED_ENTITY = ENTITY_TYPE.create(LEVEL, EntitySpawnReason.TRIGGERED);
                    if (FISHED_ENTITY != null) {
                        final Vec3 START_POS = event.getHookEntity().position();
                        final Vec3 TARGET_POS = PLAYER.position();
                        final double HORIZONTAL_DIST = Math.sqrt(START_POS.distanceToSqr(TARGET_POS.x, START_POS.y, TARGET_POS.z));

                        // needs to be self and passengers so i can add the hog rider fucker later down the line
                        FISHED_ENTITY.getSelfAndPassengers().forEach(e -> e.setPos(START_POS.x, START_POS.y, START_POS.z));

                        if (FISHED_ENTITY instanceof Mob mob) {
                            // some mobs get weird and it messes with the movement
                            mob.setNoAi(true);
                        }

                        ACTIVE_LAUNCHES.put(FISHED_ENTITY, new LaunchData(START_POS, TARGET_POS, Math.max(START_POS.y, TARGET_POS.y) + Math.max(1.0, HORIZONTAL_DIST * 0.3), LEVEL.getGameTime(), Mth.clamp((int) (HORIZONTAL_DIST / 2), 12, 100)));

                        PLAYER.sendSystemMessage(Component.translatable("fishing.messages." + ENTITY_TYPE.getDescriptionId()).withColor(0xFF55FF55));

                        if (LEVEL instanceof final ServerLevel SERVER_LEVEL) {
                            SERVER_LEVEL.tryAddFreshEntityWithPassengers(FISHED_ENTITY);
                        }

                        SKILLS.addExp(PlayerSkillsAttachment.Skill.FISHING, Objects.requireNonNullElse(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(ENTITY_TYPE).getData(UnshatteredDataMaps.FISHABLE_MOBS_EXP_DATA), 0.0f), PLAYER, UnshatteredAttributeValues.FISHING_WISDOM.holder);
                    }

                    break;
                }
            }
        } else {
            final List<TriggerMiscReward> SELECTED_ENTRIES = LEVEL.registryAccess()
                    .lookupOrThrow(DataPackRegistryHandler.FISHING_MISC_REWARD_KEY)
                    .stream()
                    .filter(reward -> UnshatteredUtils.fishingRequirementsMet(reward, (ServerPlayer) PLAYER, event.getHookEntity().blockPosition()))
                    .toList();
            totalWeight = SELECTED_ENTRIES.stream().mapToDouble(TriggerMiscReward::weight).sum();
            double currentWeight = LEVEL.getRandom().nextDouble() * totalWeight;

            for (TriggerMiscReward entry : SELECTED_ENTRIES) {
                currentWeight -= calculateAdjustedWeight(entry.weight(), FISHING_FORTUNE);
                if (currentWeight <= 0) {
                    // this (should) deal with exp and stuff itself
                    entry.trigger((ServerPlayer) PLAYER);
                    break;
                }
            }
        }

        TRIGGERED_ITEMS.forEach(item -> item.onAbilityFinished(CONTEXT));
    }

    private static double calculateAdjustedWeight(double weight, double fishingFortune) {
        return Math.pow(weight, 1.0 - ((fishingFortune / UnshatteredAttributeValues.FISHING_FORTUNE.max) * 0.5));
    }
}
