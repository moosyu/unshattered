package io.github.moosyu.abilities;

import io.github.moosyu.data.components.UnshatteredDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.ItemStack;

import java.util.List;

// for specifically abilities that increment using the INCREMENTS_STORED data component
// trigger types are often not set up for these to work so do check beforehand
public interface IncrementalAbilityItem extends AbilityItem  {
    record AttributeStage(Holder<Attribute> attributeHolder, int amountPerMilestone) {}

    List<AttributeStage> attributes();

    int maxMilestone();
    /**
     * @return the amount of whatever (blocks mined, enemies killed, etc) to reach a milestone
     */
    int perMilestoneRequirement();

    default int getMilestone(ItemStack itemStack) {
        return Math.min(itemStack.getOrDefault(UnshatteredDataComponents.INCREMENTS_STORED.get(), 0) / perMilestoneRequirement(), maxMilestone());
    }

    default void addIncrements(ItemStack itemStack, int amount) {
        int cap = maxMilestone() * perMilestoneRequirement();

        itemStack.update(UnshatteredDataComponents.INCREMENTS_STORED.get(), 0, current -> Math.min(current + amount, cap));
    }

    /**
     * @return the key for the way the increment is displayed on the tooltip eg the key for "blocks mined"
     */
    String incrementNameKey();
}
