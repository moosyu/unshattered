package io.github.moosyu.fishing.tables;

import io.github.moosyu.fishing.FishingEntry;
import io.github.moosyu.fishing.FishingItemEntry;
import io.github.moosyu.fishing.FishingMiscEntry;
import io.github.moosyu.fishing.FishingMobEntry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;

import java.util.Map;
import java.util.stream.Collectors;

import static io.github.moosyu.fishing.FishingMiscEntry.createCoinReward;

public class WaterEntries {
    public static final Map<FishingItemEntry, Double> WATER_ITEM_WEIGHTS = Map.ofEntries(
            Map.entry(new FishingItemEntry(Items.COD), 100000.0d),
            Map.entry(new FishingItemEntry(Items.SALMON), 75000.0d),
            Map.entry(new FishingItemEntry(Items.PUFFERFISH), 60000.0d),
            Map.entry(new FishingItemEntry(Items.TROPICAL_FISH), 55000.0d),
            Map.entry(new FishingItemEntry(Items.PRISMARINE_SHARD), 45000.0d),
            Map.entry(new FishingItemEntry(Items.PRISMARINE_CRYSTALS), 45000.0d),
            Map.entry(new FishingItemEntry(Items.CLAY_BALL), 45000.0d),
            Map.entry(new FishingItemEntry(Items.SPONGE), 35000.0d)
    );

    public static final Map<FishingMobEntry, Double> WATER_MOB_WEIGHTS = Map.ofEntries(
            Map.entry(new FishingMobEntry(EntityType.SQUID), 1200.0d),
            Map.entry(new FishingMobEntry(EntityType.GLOW_SQUID, player -> player.level().isDarkOutside()), 1100.0d)
    );

    public static final Map<FishingMiscEntry, Double> WATER_MISC_WEIGHTS = Map.ofEntries(
            Map.entry(createCoinReward(25000, 50001, "skills.messages.unshattered.fishing.outstanding_catch", 0xFF810AF3), 10.0d),
            Map.entry(createCoinReward(100000, 250001, "skills.messages.unshattered.fishing.outstanding_catch", 0xFFFFAA00), 5.0d),
            Map.entry(createCoinReward(500000, 1000001, "skills.messages.unshattered.fishing.outstanding_catch", 0xFFFF55FF), 1.0d)
    );

    public static double calculateTableWeight(Map<?, Double> selectedMap) {
        return selectedMap.values().stream().mapToDouble(Double::doubleValue).sum();
    }

    public static <T extends FishingEntry> Map<T, Double> filterEntries(Map<T, Double> entries, Player player) {
        return entries.entrySet().stream()
                .filter(entry -> entry.getKey().condition().test(player))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
    }


}
