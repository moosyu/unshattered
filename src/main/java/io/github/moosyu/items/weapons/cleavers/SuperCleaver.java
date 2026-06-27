package io.github.moosyu.items.weapons.cleavers;

import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.rarities.RarityTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import static io.github.moosyu.Unshattered.MODID;

public class SuperCleaver extends UnshatteredCleaver {
    public SuperCleaver(Properties properties) {
        super(properties
                .component(UnshatteredDataComponents.RARITY.get(), RarityTypes.RARE)
                .component(UnshatteredDataComponents.ITEM_ABILITY.get(), new ItemAbility("super_cleaver_cleave", 0, 0, 0, true))
                .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
                .component(UnshatteredDataComponents.ITEM_SELL_VALUE.get(), 20000)
                .attributes(ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "super_cleaver_damage"), 105, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(UnshatteredAttributeValues.STRENGTH.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "super_cleaver_strength"), 20, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(UnshatteredAttributeValues.CRITICAL_DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "super_cleaver_crit_damage"), 20, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .build()
                ),
                3.0f,
                0.3f
        );
    }
}
