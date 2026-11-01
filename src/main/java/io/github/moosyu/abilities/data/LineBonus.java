package io.github.moosyu.abilities.data;

import io.github.moosyu.abilities.Ability;
import io.github.moosyu.abilities.AbilityContext;
import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.util.UnshatteredUtils;

import java.util.Arrays;
import java.util.List;

public class LineBonus extends Ability {
    private final UnshatteredUtils.AttributeGroup[] attributeGroups;

    public LineBonus(UnshatteredUtils.AttributeGroup... attributeGroups) {
        super(new ItemAbility(), AbilityTriggerType.ONGOING);

        this.attributeGroups = attributeGroups;
    }

    @Override
    public List<UnshatteredUtils.AttributeGroup> attributeBonuses(AbilityContext context) {
        return Arrays.stream(attributeGroups).toList();
    }
}
