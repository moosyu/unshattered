package io.github.moosyu.abilities.data;

import io.github.moosyu.abilities.Ability;
import io.github.moosyu.abilities.AbilityContext;
import io.github.moosyu.abilities.AbilityContextKey;
import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.data.components.ItemAbility;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class BeachBoy extends Ability {
    public record LineAttributeGroup(Holder<Attribute> attributeHolder, AttributeModifier modifier) {}

    private final LineAttributeGroup[] attributeGroups;


    public BeachBoy(LineAttributeGroup... attributeGroups) {
        super(new ItemAbility(), AbilityTriggerType.ONGOING);

        this.attributeGroups = attributeGroups;
    }

    @Override
    public void onTrigger(AbilityContext context) {
        context.get(AbilityContextKey.PLAYER).ifPresent(player -> {
            for (LineAttributeGroup attribute : attributeGroups) {
                AttributeInstance currentAttribute = player.getAttribute(attribute.attributeHolder);
                if (currentAttribute != null) {
                    currentAttribute.addTransientModifier(attribute.modifier);
                }
            }
        });
    }

    @Override
    public void onFinish(AbilityContext context) {
        context.get(AbilityContextKey.PLAYER).ifPresent(player -> {
            for (LineAttributeGroup attribute : attributeGroups) {
                AttributeInstance currentAttribute = player.getAttribute(attribute.attributeHolder);
                if (currentAttribute != null) {
                    currentAttribute.removeModifier(attribute.modifier.id());
                }
            }
        });
    }
}
