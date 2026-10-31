package io.github.moosyu.abilities.data;

import io.github.moosyu.abilities.Ability;
import io.github.moosyu.abilities.AbilityContext;
import io.github.moosyu.abilities.AbilityContextKey;
import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class MindBlowing extends Ability {
    public MindBlowing() {
        super(new ItemAbility(), AbilityTriggerType.PLAYER_DEAL_DAMAGE);
    }

    @Override
    public boolean conditionsMet(AbilityContext context) {
        return context.get(AbilityContextKey.TARGET).map(target -> target.is(EntityTypeTags.UNDEAD)).orElse(false);
    }

    @Override
    public void onTrigger(AbilityContext context) {
        context.get(AbilityContextKey.PLAYER).flatMap(
                player -> UnshatteredUtils.getAttributeInstance(
                        player,
                        UnshatteredAttributeValues.FINAL_DAMAGE_MODIFIER.holder)
        ).ifPresent(attribute -> attribute.addTransientModifier(
                new AttributeModifier(identifier(),
                1.0,
                AttributeModifier.Operation.ADD_MULTIPLIED_BASE))
        );
    }

    @Override
    public void onFinish(AbilityContext context) {
        context.get(AbilityContextKey.PLAYER).flatMap(
                player -> UnshatteredUtils.getAttributeInstance(
                        player,
                        UnshatteredAttributeValues.FINAL_DAMAGE_MODIFIER.holder)
        ).ifPresent(attribute -> attribute.removeModifier(identifier()));
    }
}
