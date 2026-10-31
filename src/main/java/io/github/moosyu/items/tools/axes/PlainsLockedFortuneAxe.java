package io.github.moosyu.items.tools.axes;

import io.github.moosyu.abilities.*;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.regions.Region;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.List;
import java.util.function.Supplier;

// may make this more general one day but im really running low on time at the moment so just foraging fortune rn ig
public class PlainsLockedFortuneAxe extends UnshatteredAxeTool implements AbilityItem {
    private final Supplier<? extends Ability> ability;

    public PlainsLockedFortuneAxe(Properties properties, float sweepAmount, float miningSpeed, Supplier<? extends Ability> ability) {
        super(properties.attributes(ItemAttributeModifiers.builder()
                                .add(UnshatteredAttributeValues.SWEEP.holder,
                                        new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("region_locked_sweep"),
                                                sweepAmount,
                                                AttributeModifier.Operation.ADD_VALUE),
                                        EquipmentSlotGroup.MAINHAND
                                ).add(Attributes.ATTACK_SPEED, new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("region_locked_attack_speed"),
                                        -3,
                                                AttributeModifier.Operation.ADD_VALUE),
                                        EquipmentSlotGroup.MAINHAND
                                ).build()
                        ),
                miningSpeed
        );

        this.ability = ability;
    }

    @Override
    public List<? extends Supplier<? extends Ability>> abilities() {
        return List.of(ability);
    }
}
