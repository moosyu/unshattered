package io.github.moosyu.items.talismans;

import io.github.moosyu.abilities.Ability;
import io.github.moosyu.abilities.AbilityItem;
import io.github.moosyu.util.UnshatteredUtils;

import java.util.List;
import java.util.function.Supplier;

public class PresentSnowGlobeBase extends TieredTalisman implements AbilityItem {
    private final Supplier<? extends Ability> ability;

    public PresentSnowGlobeBase(Properties properties, Supplier<? extends Ability> ability) {
        super(properties, UnshatteredUtils.getUnshatteredIdentifier("present_snow_globe_path"));

        this.ability = ability;
    }

    @Override
    public List<? extends Supplier<? extends Ability>> abilities() {
        return List.of(ability);
    }
}
