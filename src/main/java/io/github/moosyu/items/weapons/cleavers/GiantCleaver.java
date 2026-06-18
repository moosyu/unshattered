package io.github.moosyu.items.weapons.cleavers;

import io.github.moosyu.attributes.UnshatteredAttributes;
import io.github.moosyu.data.components.DataComponentRegistry;
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
                .component(DataComponentRegistry.RARITY.get(), RarityTypes.LEGENDARY)
                .component(DataComponentRegistry.ITEM_ABILITY.get(), new ItemAbility("giant_cleaver_cleave", 0, 0, 0, true)).attributes(ItemAttributeModifiers.builder().add(UnshatteredAttributes.DAMAGE.holder,
                new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "giant_cleaver_damage"), 235, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).add(UnshatteredAttributes.STRENGTH.holder,
                new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "giant_cleaver_strength"), 120, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).add(UnshatteredAttributes.CRITICAL_DAMAGE.holder,
                new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "giant_cleaver_crit_damage"), 120, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build()),
                5.0f
        );
    }
}
