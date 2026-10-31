package io.github.moosyu.items;

import io.github.moosyu.abilities.Ability;
import io.github.moosyu.abilities.AbilityItem;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.function.Supplier;

public class BaseAbilityItem extends Item implements AbilityItem {
    Supplier<? extends Ability> ability;

    public BaseAbilityItem(Properties properties, Supplier<? extends Ability> ability) {
        super(properties.stacksTo(1));

        this.ability = ability;
    }

    @Override
    public List<? extends Supplier<? extends Ability>> abilities() {
        return List.of(ability);
    }
}
