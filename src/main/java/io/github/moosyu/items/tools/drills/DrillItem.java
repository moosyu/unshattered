package io.github.moosyu.items.tools.drills;

import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.components.ItemFuel;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.ItemType;
import io.github.moosyu.items.UnshatteredRarity;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class DrillItem extends Item {
    public DrillItem(Properties properties, int damage, int miningSpeed, int miningFortune, int breakingPower, String identifier, int sellValue, UnshatteredRarity rarity) {
        super(properties.component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemType.DRILL)
                .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
                .component(UnshatteredDataComponents.SELL_VALUE.get(), sellValue)
                .component(UnshatteredDataComponents.FUEL.get(), new ItemFuel(3000, 3000))
                .component(UnshatteredDataComponents.RARITY.get(), rarity)
                .attributes(ItemAttributeModifiers.builder()
                        .add(UnshatteredAttributeValues.DAMAGE.holder,
                                new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier(identifier + "_damage"), damage, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND
                        ).add(UnshatteredAttributeValues.MINING_SPEED.holder,
                                new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier(identifier + "_mining_speed"), miningSpeed, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND
                        ).add(UnshatteredAttributeValues.MINING_FORTUNE.holder,
                                new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier(identifier + "_mining_fortune"), miningFortune, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND
                        ).add(UnshatteredAttributeValues.BREAKING_POWER.holder,
                                new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier(identifier + "_breaking_power"), breakingPower, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND
                        ).add(Attributes.ATTACK_SPEED,
                                new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier(identifier + "_attack_speed"), -2.8, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND
                        ).build()
                )
        );
    }
}
