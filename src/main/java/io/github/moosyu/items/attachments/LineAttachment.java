package io.github.moosyu.items.attachments;

import io.github.moosyu.abilities.AbilityContext;
import io.github.moosyu.abilities.AbilityContextKey;
import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.abilities.PassiveAbilityItem;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;

import java.util.Set;

public class LineAttachment extends Item implements PassiveAbilityItem {
    // almost certain this exists somewhere but until i can remember where that is we have this
    public record LineAttributeGroup(Holder<Attribute> attributeHolder, AttributeModifier modifier) {}

    private final LineAttributeGroup[] attributeGroups;

    public LineAttachment(Properties properties, LineAttributeGroup... attributeGroups) {
        super(properties.stacksTo(1));

        this.attributeGroups = attributeGroups;
    }

    @Override
    public Set<AbilityTriggerType> triggerTypes() {
        return Set.of(AbilityTriggerType.ONGOING);
    }

    @Override
    public void onAbilityTriggered(AbilityContext context) {
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
    public void onAbilityFinished(AbilityContext context) {
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
