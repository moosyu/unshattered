package io.github.moosyu.items.tools.axes;

import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.abilities.IncrementalAbilityItem;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.ItemTypes;
import io.github.moosyu.items.UnshatteredRarities;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Set;

public class PromisingAxe extends Item implements IncrementalAbilityItem {
    public PromisingAxe(Properties properties) {
        super(properties.stacksTo(1)
                .attributes(ItemAttributeModifiers.builder()
                        .add(UnshatteredAttributeValues.DAMAGE.holder,
                                new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("promising_axe_damage"), 2, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND
                        ).add(UnshatteredAttributeValues.SWEEP.holder,
                                new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("promising_axe_sweep"), 1, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND
                        ).add(UnshatteredAttributeValues.BREAKING_POWER.holder,
                                new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("promising_axe_breaking_power"), 2, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND
                        ).add(Attributes.ATTACK_SPEED,
                                new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("promising_axe_attack_speed"), -3, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND
                        ).build()
                )
                .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.AXE)
                .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
                .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.UNCOMMON)
                .component(UnshatteredDataComponents.SELL_VALUE.get(), 10)
                .component(UnshatteredDataComponents.INCREMENTS_STORED.get(), 0)
                .component(UnshatteredDataComponents.ABILITY.get(), new ItemAbility(UnshatteredUtils.getUnshatteredIdentifier("axe_stored_potential"), 0, 0, 0, true))
        );
    }

    @Override
    public List<AttributeStage> attributes() {
        return List.of(new AttributeStage(UnshatteredAttributeValues.SWEEP.holder, 1));
    }

    @Override
    public int maxMilestone() {
        return 15;
    }

    @Override
    public int perMilestoneRequirement() {
        return 200;
    }

    @Override
    public String incrementNameKey() {
        return "tooltip.unshattered.incremental.blocks_broken";
    }

    @Override
    public Set<AbilityTriggerType> triggerTypes() {
        return Set.of(AbilityTriggerType.PLAYER_BREAK_SWEEP_BLOCK);
    }

    // so that changing block break count doesn't do the re-equip animation
    @Override
    public boolean shouldCauseReequipAnimation(@NonNull ItemStack oldStack, @NonNull ItemStack newStack, boolean slotChanged) {
        return slotChanged || oldStack.getItem() != newStack.getItem();
    }
}
