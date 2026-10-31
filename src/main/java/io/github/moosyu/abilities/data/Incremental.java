package io.github.moosyu.abilities.data;

import io.github.moosyu.abilities.Ability;
import io.github.moosyu.abilities.AbilityContext;
import io.github.moosyu.abilities.AbilityContextKey;
import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class Incremental extends Ability {
    public record AttributeStage(Holder<Attribute> attributeHolder, int amountPerMilestone) {}

    private final List<AttributeStage> attributes;
    private final int maxMilestone;
    private final int perMilestoneRequirement;
    private final String incrementNameKey;

    public Incremental(List<AttributeStage> attributes, int maxMilestone, int perMilestoneRequirement, String incrementNameKey, AbilityTriggerType abilityTriggerType) {
        super(new ItemAbility(), abilityTriggerType);
        this.attributes = List.copyOf(attributes);
        this.maxMilestone = maxMilestone;
        this.perMilestoneRequirement = perMilestoneRequirement;
        this.incrementNameKey = incrementNameKey;
    }

    /**
     * @return how much one trigger is worth
     */
    private int incrementAmount() {
        return 1;
    }

    @Override
    public void onTrigger(AbilityContext context) {
        context.get(AbilityContextKey.ITEM_STACK).ifPresent(itemStack -> addIncrements(itemStack, incrementAmount()));
    }

    public int getMilestone(ItemStack itemStack) {
        return Math.min(itemStack.getOrDefault(UnshatteredDataComponents.INCREMENTS_STORED.get(), 0) / perMilestoneRequirement, maxMilestone);
    }

    public void addIncrements(ItemStack itemStack, int amount) {
        int cap = maxMilestone() * perMilestoneRequirement();
        itemStack.update(UnshatteredDataComponents.INCREMENTS_STORED.get(), 0, current -> Math.min(current + amount, cap));
    }

    public List<AttributeStage> attributes() {
        return attributes;
    }

    public int maxMilestone() {
        return maxMilestone;
    }

    public int perMilestoneRequirement() {
        return perMilestoneRequirement;
    }

    public String incrementNameKey() {
        return incrementNameKey;
    }
}