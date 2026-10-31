package io.github.moosyu.items.attachments;

import io.github.moosyu.abilities.*;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.Set;
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
