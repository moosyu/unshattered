package io.github.moosyu.items.tools.rods;

import io.github.moosyu.abilities.Ability;
import io.github.moosyu.abilities.AbilityItem;
import io.github.moosyu.abilities.UnshatteredAbilities;

import java.util.List;
import java.util.function.Supplier;

public class UnshatteredWaterFishingRod extends UnshatteredFishingRod implements AbilityItem {
    public UnshatteredWaterFishingRod(Properties properties) {
        super(properties);
    }

    @Override
    public List<? extends Supplier<? extends Ability>> abilities() {
        return List.of(UnshatteredAbilities.FISH_OUT_OF_WATER);
    }
}
