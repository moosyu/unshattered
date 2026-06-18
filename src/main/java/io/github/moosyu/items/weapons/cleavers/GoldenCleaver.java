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

public class GoldenCleaver extends UnshatteredCleaver {
    public GoldenCleaver(Properties properties) {
        super(properties
                .component(DataComponentRegistry.RARITY.get(), RarityTypes.UNCOMMON)
                .component(DataComponentRegistry.ITEM_ABILITY.get(), new ItemAbility("golden_cleaver_cleave", 0, 0, 0, true))
                .attributes(ItemAttributeModifiers.builder()
                .add(UnshatteredAttributes.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "golden_cleaver_damage"), 40, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(UnshatteredAttributes.STRENGTH.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "golden_cleaver_strength"), 10, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build()),
                3.0f
        );
    }
}
