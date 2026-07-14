package io.github.moosyu.collectables;

import io.github.moosyu.attachments.PlayerSkillsAttachment;
import io.github.moosyu.collectables.rewards.ExperienceCollectableReward;
import io.github.moosyu.collectables.rewards.ItemCollectableReward;
import io.github.moosyu.items.UnshatteredItems;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public final class CollectableEntries {
    public static final Map<Holder<Item>, CollectableItemEntry> COLLECTABLE_ENTRIES = Map.ofEntries(
            Map.entry(BuiltInRegistries.ITEM.wrapAsHolder(Items.ROTTEN_FLESH),
                    new CollectableItemEntry(CollectableCategories.COMBAT, Items.ROTTEN_FLESH,
                            List.of(new CollectableLevel(50,
                                    List.of(new ExperienceCollectableReward(250, PlayerSkillsAttachment.Skill.COMBAT))
                            ), new CollectableLevel(100,
                                    List.of(new ItemCollectableReward(UnshatteredItems.ENCHANTED_ROTTEN_FLESH.get(), 1)))
                            )
                    )
            )
    );

    @Nullable
    public static CollectableItemEntry getCollectableEntry(Holder<Item> itemHolder) {
        return COLLECTABLE_ENTRIES.getOrDefault(itemHolder, null);
    }
}
