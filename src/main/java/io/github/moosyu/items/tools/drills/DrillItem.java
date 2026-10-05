package io.github.moosyu.items.tools.drills;

import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.components.ItemAttachments;
import io.github.moosyu.data.components.ItemFuel;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.ItemType;
import io.github.moosyu.items.UnshatteredRarity;
import io.github.moosyu.items.tools.UnshatteredMiningToolBase;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.jspecify.annotations.NonNull;

public class DrillItem extends UnshatteredMiningToolBase {
    public DrillItem(Properties properties, int damage, int miningSpeed, int miningFortune, int breakingPower, String identifier) {
        super(properties.stacksTo(1)
                .component(UnshatteredDataComponents.FUEL.get(), new ItemFuel(3000, 3000))
                .component(UnshatteredDataComponents.ITEM_ATTACHMENTS.get(),ItemAttachments.withSlots(ItemAttachments.SlotType.FUEL_TANK,
                        ItemAttachments.SlotType.DRILL_ENGINE,
                        ItemAttachments.SlotType.UPGRADE_MODULE)
                )
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

    // should hopefully fix the issue like on hypixel where updating the fuel causes the breaking of the next item to
    // reset. in most cases it wont matter because ping shouldnt be an issue but you never know.
    @Override
    public boolean shouldCauseBlockBreakReset(@NonNull ItemStack oldStack, ItemStack newStack) {
        // very smart but also very dangerous, however i think drill swapping is cool and this is fast so
        // im just checking this way
        return !(newStack.getItem() instanceof DrillItem) || !(oldStack.getItem() instanceof DrillItem);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        ItemFuel fuel = stack.get(UnshatteredDataComponents.FUEL.get());
        if (fuel == null) return false;

        return fuel.currentFuel() < fuel.getMaxFuel(stack);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        ItemFuel fuel = stack.get(UnshatteredDataComponents.FUEL.get());
        if (fuel == null) return 0;

        int max = fuel.getMaxFuel(stack);
        if (max <= 0) return 0;

        return Math.round(13.0f * Mth.clamp((float) fuel.currentFuel() / max, 0.0f, 1.0f));
    }

    @Override
    public int getBarColor(@NonNull ItemStack stack) {
        return 0xFFFF6A00;
    }
}
