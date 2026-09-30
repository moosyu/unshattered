package io.github.moosyu.items.weapons.shortbows;

import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.UnshatteredRarity;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class ArtisanalShortbow extends ShortbowItem {
    public ArtisanalShortbow(Properties properties) {
        super(properties.component(UnshatteredDataComponents.SELL_VALUE.get(), 100)
                .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarity.UNCOMMON)
                .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
                .attributes(ItemAttributeModifiers.builder()
                        .add(UnshatteredAttributeValues.DAMAGE.holder,
                                new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("artisanal_shortbow"),
                                        2,
                                        AttributeModifier.Operation.ADD_VALUE
                                ),
                                EquipmentSlotGroup.MAINHAND
                        )
                        .build()
                )
        );
    }
}
