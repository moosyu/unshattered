package io.github.moosyu.abilities.data;

import io.github.moosyu.abilities.Ability;
import io.github.moosyu.abilities.AbilityContext;
import io.github.moosyu.abilities.AbilityContextKey;
import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;

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

    @Override
    public boolean conditionsMet(AbilityContext context) {
        Optional<ItemStack> source = context.get(AbilityContextKey.ITEM_STACK);
        Optional<ServerPlayer> player = context.get(AbilityContextKey.PLAYER);
        return source.isPresent() && player.isPresent() && source.get() == player.get().getMainHandItem();
    }

    @Override
    public void onTrigger(AbilityContext context) {
        int amount = context.get(AbilityContextKey.BROKEN_COUNT).orElse(1);
        context.get(AbilityContextKey.ITEM_STACK).ifPresent(itemStack -> addIncrements(itemStack, amount));
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