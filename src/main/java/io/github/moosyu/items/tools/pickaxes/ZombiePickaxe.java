package io.github.moosyu.items.tools.pickaxes;

import io.github.moosyu.abilities.*;
import io.github.moosyu.attributes.UnshatteredAttributeValue;
import io.github.moosyu.items.tools.UnshatteredMiningToolBase;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.List;
import java.util.function.Supplier;

public class ZombiePickaxe extends UnshatteredMiningToolBase implements AbilityItem {
    public ZombiePickaxe(Properties properties) {
        super(properties.stacksTo(1)
                .attributes(ItemAttributeModifiers.builder()
                        .add(UnshatteredAttributeValue.DAMAGE.holder,
                                new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("zombie_pickaxe_damage"), 5, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND
                        ).add(UnshatteredAttributeValue.MINING_SPEED.holder,
                                new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("zombie_pickaxe_mining_speed"), 3, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND
                        ).add(UnshatteredAttributeValue.BREAKING_POWER.holder,
                                new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("zombie_pickaxe_breaking_power"), 3, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND
                        ).add(Attributes.ATTACK_SPEED,
                                new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("zombie_pickaxe_attack_speed"), -2.8, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND
                        ).build()
                )
        );
    }

    @Override
    public List<? extends Supplier<? extends Ability>> abilities() {
        return List.of(UnshatteredAbilities.ROTTEN);
    }
}
