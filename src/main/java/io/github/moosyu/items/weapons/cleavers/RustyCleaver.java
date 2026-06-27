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

public class RustyCleaver extends UnshatteredCleaver {
    public RustyCleaver(Properties properties) {
        super(properties
                .component(UnshatteredDataComponents.RARITY.get(), RarityTypes.COMMON)
                .component(UnshatteredDataComponents.ITEM_ABILITY.get(), new ItemAbility("rusty_cleaver_cleave", 0, 0, 0, true))
                .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
                .component(UnshatteredDataComponents.ITEM_SELL_VALUE.get(), 8)
                .attributes(ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "rusty_cleaver_damage"), 30, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                        .build()
                ),
            2.0f,
            0.1f
        );
    }
}
