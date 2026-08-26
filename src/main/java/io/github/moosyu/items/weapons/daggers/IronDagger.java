package io.github.moosyu.items.weapons.daggers;

import io.github.moosyu.attributes.UnshatteredAttributeValues;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import static io.github.moosyu.Unshattered.MODID;

public class IronDagger extends UnshatteredDagger {
    public IronDagger(Properties properties) {
        super(properties.attributes(ItemAttributeModifiers.builder()
                .add(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "iron_dagger_damage"), 25, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(UnshatteredAttributeValues.FEROCITY.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "iron_dagger_ferocity"), 25, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "iron_dagger_attack_speed"), 8, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .build()
        ));
    }
}
