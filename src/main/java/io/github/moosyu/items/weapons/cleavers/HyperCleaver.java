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

public class HyperCleaver extends UnshatteredCleaver {
    public HyperCleaver(Properties properties) {
        super(properties
                .component(UnshatteredDataComponents.RARITY.get(), RarityTypes.EPIC)
                .component(UnshatteredDataComponents.ITEM_ABILITY.get(), new ItemAbility("hyper_cleaver_cleave", 0, 0, 0, true))
                .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
                .component(UnshatteredDataComponents.ITEM_SELL_VALUE.get(), 200000)
                .attributes(ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "hyper_cleaver_damage"), 175, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(UnshatteredAttributeValues.STRENGTH.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "hyper_cleaver_strength"), 100, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(UnshatteredAttributeValues.CRITICAL_DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "hyper_cleaver_crit_damage"), 100, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .build()
                ),
                4.0f,
                0.4f
        );
    }
}
