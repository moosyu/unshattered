package io.github.moosyu.skills.experience;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import java.util.Map;

public class ItemsFishingExperience {
    private static final Map<Item, Float> ITEMS_FISHING_EXP = Map.ofEntries(
            Map.entry(Items.COD, 0.5f),
            Map.entry(Items.SALMON, 0.7f),
            Map.entry(Items.PUFFERFISH, 1.0f),
            Map.entry(Items.TROPICAL_FISH, 2.0f),
            Map.entry(Items.PRISMARINE_CRYSTALS, 0.5f),
            Map.entry(Items.PRISMARINE_SHARD, 0.5f),
            Map.entry(Items.SPONGE, 0.5f),
            Map.entry(Items.CLAY_BALL, 0.1f)
    );

    public static float getExp(Item fishingDrop) {
        return ITEMS_FISHING_EXP.getOrDefault(fishingDrop, 0.0f);
    }
}