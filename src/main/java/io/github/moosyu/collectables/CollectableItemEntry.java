package io.github.moosyu.collectables;

import net.minecraft.world.item.Item;

import java.util.List;

public record CollectableItemEntry(CollectableCategories category, Item item, List<CollectableLevel> levels) {}
