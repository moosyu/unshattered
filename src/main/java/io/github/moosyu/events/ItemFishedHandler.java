package io.github.moosyu.events;

import io.github.moosyu.Unshattered;
import io.github.moosyu.abilities.*;
import io.github.moosyu.data.attachments.PlayerSkillsAttachment;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.UnshatteredDataMaps;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.fishing.FishingWeightEntry;
import io.github.moosyu.data.fishing.FishingWeightModifier;
import io.github.moosyu.data.fishing.TriggerMiscReward;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
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
import java.util.function.ToDoubleFunction;

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
        if (level.isClientSide() || !(player instanceof ServerPlayer serverPlayer)) {
            return;
        }

        event.setCanceled(true);

        AbilityContext context = new AbilityContext().add(AbilityContextKey.PLAYER, serverPlayer);
        try (AbilityScope scope = AbilityScope.open(serverPlayer, AbilityTriggerType.PLAYER_FISH, context)) {
            if (scope.results.get(AbilityResult.CANCEL_EVENT)) {
                return;
            }

            AttributeInstance fishingFortuneAttribute = player.getAttribute(UnshatteredAttributeValues.FISHING_FORTUNE.holder);
            if (fishingFortuneAttribute == null) {
                return;
            }

            PlayerSkillsAttachment skills = player.getData(UnshatteredAttachments.PLAYER_SKILLS.get());
            RandomSource random = level.getRandom();
            BlockPos hookPos = event.getHookEntity().blockPosition();
            int selectedTableRange = random.nextInt(200);
            double fishingFortune = fishingFortuneAttribute.getValue();

            if (selectedTableRange < 160) { // item
                Map<ResourceKey<Item>, FishingWeightEntry> selectedMap = UnshatteredUtils.filterFishingEntries(BuiltInRegistries.ITEM.getDataMap(UnshatteredDataMaps.FISHING_ITEM_WEIGHT_DATA), serverPlayer, hookPos);

                Map<ResourceKey<Item>, Double> weights = buildAdjustedWeights(selectedMap, fishingFortune, scope.results.get(AbilityResult.MODIFY_FISHING_ITEM_WEIGHTS));
                Map.Entry<ResourceKey<Item>, Double> pickedEntry = pickWeighted(weights.entrySet(), Map.Entry::getValue, random);

                if (pickedEntry != null) {
                    ResourceKey<Item> pickedKey = pickedEntry.getKey();
                    Item selectedItem = BuiltInRegistries.ITEM.getValue(pickedKey);
                    if (selectedItem == null) {
                        Unshattered.LOGGER.error("{} is null!!", pickedKey);
                        return;
                    }

                    UnshatteredUtils.addItemStackToInventory(player, new ItemStack(selectedItem, UnshatteredUtils.getItemsCount(fishingFortune, 1)));

                    float expReward = Objects.requireNonNullElse(BuiltInRegistries.ITEM.wrapAsHolder(selectedItem).getData(UnshatteredDataMaps.FISHABLE_ITEMS_EXP_DATA), 0.0f);
                    if (expReward > 0.0f) {
                        skills.addExp(PlayerSkillsAttachment.Skill.FISHING, expReward, player, UnshatteredAttributeValues.FISHING_WISDOM.holder);
                        player.syncData(PLAYER_SKILLS);
                    }
                }
            } else if (selectedTableRange < 194) { // mobs
                Map<ResourceKey<EntityType<?>>, FishingWeightEntry> selectedMap = UnshatteredUtils.filterFishingEntries(
                        BuiltInRegistries.ENTITY_TYPE.getDataMap(UnshatteredDataMaps.FISHING_MOB_WEIGHT_DATA),
                        serverPlayer,
                        hookPos
                );

                Map<ResourceKey<EntityType<?>>, Double> weights = buildAdjustedWeights(selectedMap, fishingFortune, scope.results.get(AbilityResult.MODIFY_FISHING_MOB_WEIGHTS));
                Map.Entry<ResourceKey<EntityType<?>>, Double> pickedEntry = pickWeighted(weights.entrySet(), Map.Entry::getValue, random);

                if (pickedEntry != null) {
                    ResourceKey<EntityType<?>> key = pickedEntry.getKey();
                    EntityType<?> entityType = BuiltInRegistries.ENTITY_TYPE.getValue(key);
                    if (entityType == null) {
                        Unshattered.LOGGER.error("{} is null", key);
                        return;
                    }

                    Entity fishedEntity = entityType.create(level, EntitySpawnReason.TRIGGERED);
                    if (fishedEntity != null) {
                        Vec3 startPos = event.getHookEntity().position();
                        Vec3 targetPos = player.position();
                        double horizontalDist = Math.sqrt(startPos.distanceToSqr(targetPos.x, startPos.y, targetPos.z));

                        // needs to be self and passengers so i can add the hog rider fucker later down the line
                        fishedEntity.getSelfAndPassengers().forEach(entity -> entity.setPos(startPos.x, startPos.y, startPos.z));

                        if (fishedEntity instanceof Mob mob) {
                            // some mobs get weird and it messes with the movement
                            mob.setNoAi(true);
                        }

                        ACTIVE_LAUNCHES.put(fishedEntity, new LaunchData(
                                startPos,
                                targetPos,
                                Math.max(startPos.y, targetPos.y) + Math.max(1.0, horizontalDist * 0.3),
                                level.getGameTime(),
                                Mth.clamp((int) (horizontalDist / 2), 12, 100)));

                        player.sendSystemMessage(Component.translatable("fishing.messages." + entityType.getDescriptionId()).withColor(0xFF55FF55));

                        if (level instanceof ServerLevel serverLevel) {
                            serverLevel.tryAddFreshEntityWithPassengers(fishedEntity);
                        }

                        skills.addExp(
                                PlayerSkillsAttachment.Skill.FISHING,
                                Objects.requireNonNullElse(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(entityType).getData(UnshatteredDataMaps.FISHABLE_MOBS_EXP_DATA), 0.0f),
                                player,
                                UnshatteredAttributeValues.FISHING_WISDOM.holder);
                    }
                }
            } else { // misc
                List<TriggerMiscReward> selectedEntries = level.registryAccess()
                        .lookupOrThrow(DataPackRegistryHandler.FISHING_MISC_REWARD_KEY)
                        .stream()
                        .filter(reward -> UnshatteredUtils.fishingRequirementsMet(reward, serverPlayer, hookPos))
                        .toList();

                TriggerMiscReward picked = pickWeighted(selectedEntries, reward -> calculateAdjustedWeight(reward.weight(), fishingFortune), random);
                if (picked != null) {
                    // this (should) deal with exp and stuff itself
                    picked.trigger(serverPlayer);
                }
            }
        }
    }

    private static <T> T pickWeighted(Collection<T> entries, ToDoubleFunction<T> weightFunction, RandomSource random) {
        if (entries.isEmpty()) {
            return null;
        }

        double total = 0.0;
        for (T entry : entries) {
            total += weightFunction.applyAsDouble(entry);
        }

        double roll = random.nextDouble() * total;
        T last = null;
        for (T entry : entries) {
            last = entry;
            roll -= weightFunction.applyAsDouble(entry);
            if (roll <= 0) {
                return entry;
            }
        }
        return last;
    }

    private static <T extends ResourceKey<?>> Map<T, Double> buildAdjustedWeights(
            Map<T, FishingWeightEntry> entries,
            double fishingFortune,
            FishingWeightModifier modifier
    ) {
        // abilities run before the table is filtered, so they can't know which entries are "top x%".
        // instead each entry gets a rank fraction by raw weight (0 = heaviest) for modifiers to check
        List<T> ranked = new ArrayList<>(entries.keySet());
        ranked.sort((a, b) -> Double.compare(entries.get(b).weight(), entries.get(a).weight()));

        Map<T, Double> ranks = new HashMap<>();
        for (int i = 0; i < ranked.size(); i++) {
            ranks.put(ranked.get(i), (double) i / ranked.size());
        }

        Map<T, Double> result = new LinkedHashMap<>();
        for (Map.Entry<T, FishingWeightEntry> entry : entries.entrySet()) {
            double weight = calculateAdjustedWeight(entry.getValue().weight(), fishingFortune);
            result.put(entry.getKey(), modifier.apply(entry.getKey(), weight, ranks.get(entry.getKey())));
        }

        return result;
    }

    private static double calculateAdjustedWeight(double weight, double fishingFortune) {
        return Math.pow(weight, 1.0 - ((fishingFortune / UnshatteredAttributeValues.FISHING_FORTUNE.max) * 0.5));
    }
}