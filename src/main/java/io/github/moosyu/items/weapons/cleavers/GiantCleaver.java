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

public class GiantCleaver extends UnshatteredCleaver {
    public GiantCleaver(Properties properties) {
        super(properties
                .component(UnshatteredDataComponents.RARITY.get(), RarityTypes.LEGENDARY)
                .component(UnshatteredDataComponents.ITEM_ABILITY.get(), new ItemAbility("giant_cleaver_cleave", 0, 0, 0, true))
                .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
                .component(UnshatteredDataComponents.ITEM_SELL_VALUE.get(), 200000)
                .attributes(ItemAttributeModifiers.builder().add(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "giant_cleaver_damage"), 235, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(UnshatteredAttributeValues.STRENGTH.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "giant_cleaver_strength"), 120, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(UnshatteredAttributeValues.CRITICAL_DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "giant_cleaver_crit_damage"), 120, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .build()
                ),
                5.0f,
                0.5f
        );
    }
}
