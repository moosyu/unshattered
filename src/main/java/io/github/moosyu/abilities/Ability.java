package io.github.moosyu.abilities;

import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import javax.annotation.Nullable;
import java.util.List;

public class Ability {
    public final ItemAbility data;
    public final @Nullable AbilityTriggerType trigger;
    /**
     * ability results run when the ability is successfully triggered
     */
    public final AbilityResults abilityResults;

    protected Ability(ItemAbility data, @Nullable AbilityTriggerType trigger, AbilityResults abilityResults) {
        this.data = data;
        this.trigger = trigger;
        this.abilityResults = abilityResults;
    }

    protected Ability(ItemAbility data, @Nullable AbilityTriggerType trigger) {
        this(data, trigger, new AbilityResults());
    }

    public final Identifier id() {
        return UnshatteredAbilities.REGISTRY.getKey(this);
    }

    public final boolean isTicking() {
        return trigger == AbilityTriggerType.ONGOING || trigger == AbilityTriggerType.TICKED;
    }

    /**
     * conditionsMet is run clientside for the tooltips so act accordingly
     */
    public boolean conditionsMet(AbilityContext context) {
        return true;
    }

    /**
     * adds attribute bonuses and runs {@link #onTrigger(AbilityContext)}, should be used instead of {@link #onTrigger(AbilityContext)} for firing abilities off
     */
    public void activate(AbilityContext context) {
        applyBonuses(context);
        onTrigger(context);
    }

    /**
     * removes attribute bonuses and runs {@link #onFinish(AbilityContext)}, should be used instead of {@link #onFinish(AbilityContext)} for turning off abilities
     */
    public void deactivate(AbilityContext context) {
        removeBonuses(context);
        onFinish(context);
    }

    public void applyBonuses(AbilityContext context) {
        context.get(AbilityContextKey.PLAYER).ifPresent(player -> {
            for (UnshatteredUtils.AttributeGroup group : attributeBonuses(context)) {
                AttributeInstance attribute = player.getAttribute(group.attributeHolder());
                if (attribute == null) continue;

                AttributeModifier existing = attribute.getModifier(group.modifier().id());
                if (!group.modifier().equals(existing)) {
                    attribute.addOrUpdateTransientModifier(group.modifier());
                }
            }
        });
    }

    public final void removeBonuses(AbilityContext context) {
        context.get(AbilityContextKey.PLAYER).ifPresent(player -> {
            for (UnshatteredUtils.AttributeGroup group : attributeBonuses(context)) {
                AttributeInstance attribute = player.getAttribute(group.attributeHolder());
                if (attribute != null) {
                    attribute.removeModifier(group.modifier().id());
                }
            }
        });
    }

    protected void onTrigger(AbilityContext context) {}

    protected void onFinish(AbilityContext context) {}

    /**
     * attributeBonuses is run clientside for the tooltips so act accordingly
     */
    public List<UnshatteredUtils.AttributeGroup> attributeBonuses(AbilityContext context) {
        return List.of();
    }
}