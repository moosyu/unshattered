package io.github.moosyu.items.talismans;

import io.github.moosyu.abilities.*;
import io.github.moosyu.util.UnshatteredUtils;

import java.util.List;
import java.util.function.Supplier;

public class BatTalismanBase extends TieredTalisman implements AbilityItem {
    private final Supplier<? extends Ability> ability;

    public BatTalismanBase(Properties properties, Supplier<? extends Ability> ability) {
        super(properties, UnshatteredUtils.getUnshatteredIdentifier("bat_talisman_path"));

        this.ability = ability;
    }

    @Override
    public List<? extends Supplier<? extends Ability>> abilities() {
        return List.of(ability);
    }
}
