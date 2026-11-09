package io.github.moosyu.items.attachments;

import io.github.moosyu.abilities.*;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.function.Supplier;

public class DrillEngineAttachment extends Item implements AbilityItem {
    private final Supplier<? extends Ability> ability;

    public DrillEngineAttachment(Properties properties, Supplier<? extends Ability> ability) {
        super(properties.stacksTo(1));

        this.ability = ability;
    }


    @Override
    public List<? extends Supplier<? extends Ability>> abilities() {
        return List.of(ability);
    }
}
