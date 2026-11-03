package io.github.moosyu.items.attachments.hooks;

import io.github.moosyu.abilities.Ability;
import io.github.moosyu.abilities.AbilityItem;
import io.github.moosyu.abilities.UnshatteredAbilities;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.function.Supplier;

public class CommonHook extends Item implements AbilityItem {
    public CommonHook(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public List<? extends Supplier<? extends Ability>> abilities() {
        return List.of(UnshatteredAbilities.LOWEST_COMMON_DENOMINATOR);
    }
}
