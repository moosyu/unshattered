package io.github.moosyu.items.attachments;

import io.github.moosyu.abilities.*;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.ItemType;
import io.github.moosyu.items.UnshatteredRarity;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public class FriedGoblinEggAttachment extends Item implements AbilityItem {
    private final Supplier<? extends Ability> ability;

    public FriedGoblinEggAttachment(Properties properties, Supplier<? extends Ability> ability) {
        super(properties.stacksTo(1));

        this.ability = ability;
    }

    @Override
    public List<? extends Supplier<? extends Ability>> abilities() {
        return List.of(ability);
    }
}
