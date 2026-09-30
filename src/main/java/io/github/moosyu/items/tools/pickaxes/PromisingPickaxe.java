package io.github.moosyu.items.tools.pickaxes;

import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.abilities.IncrementalAbilityItem;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.ItemType;
import io.github.moosyu.items.UnshatteredRarity;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.List;
import java.util.Set;

public class PromisingPickaxe extends Item implements IncrementalAbilityItem {
    public PromisingPickaxe(Properties properties) {
        super(properties.stacksTo(1)
                .attributes(ItemAttributeModifiers.builder()
                        .add(UnshatteredAttributeValues.DAMAGE.holder,
                                new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("promising_pickaxe_damage"), 2, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND
                        ).add(UnshatteredAttributeValues.MINING_SPEED.holder,
                                new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("promising_pickaxe_mining_speed"), 3, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND
                        ).add(UnshatteredAttributeValues.BREAKING_POWER.holder,
                                new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("promising_pickaxe_breaking_power"), 3, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND
                        ).add(Attributes.ATTACK_SPEED,
                                new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("promising_pickaxe_attack_speed"), -2.8, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND
                        ).build()
                )
                .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemType.PICKAXE)
                .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
                .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarity.UNCOMMON)
                .component(UnshatteredDataComponents.SELL_VALUE.get(), 10)
                .component(UnshatteredDataComponents.INCREMENTS_STORED.get(), 0)
                .component(UnshatteredDataComponents.ABILITY.get(), new ItemAbility(UnshatteredUtils.getUnshatteredIdentifier("stored_potential"), 0, 0, 0, true))
        );
    }

    @Override
    public List<AttributeStage> attributes() {
        return List.of(new AttributeStage(UnshatteredAttributeValues.MINING_SPEED.holder, 3));
    }

    @Override
    public int maxMilestone() {
        return 25;
    }

    @Override
    public int perMilestoneRequirement() {
        return 100;
    }

    @Override
    public String incrementNameKey() {
        return "tooltip.unshattered.incremental.blocks_broken";
    }

    @Override
    public Set<AbilityTriggerType> triggerTypes() {
        return Set.of(AbilityTriggerType.PLAYER_BREAK_MINING_BLOCK);
    }
}