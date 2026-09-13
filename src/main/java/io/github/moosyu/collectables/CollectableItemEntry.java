package io.github.moosyu.collectables;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

import java.util.List;

public record CollectableItemEntry(CollectableCategories category, Item item, List<CollectableLevel> levels) {
    public static Codec<CollectableItemEntry> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    CollectableCategories.CODEC.fieldOf("category").forGetter(CollectableItemEntry::category),
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(CollectableItemEntry::item),
                    CollectableLevel.CODEC.listOf().fieldOf("levels").forGetter(CollectableItemEntry::levels)
            ).apply(instance, CollectableItemEntry::new)
    );
}
