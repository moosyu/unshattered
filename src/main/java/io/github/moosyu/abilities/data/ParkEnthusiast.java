package io.github.moosyu.abilities.data;

import io.github.moosyu.abilities.*;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.data.regions.UnshatteredRegions;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.List;

public class ParkEnthusiast extends Ability {
    private final float foragingFortuneAmount;

    public ParkEnthusiast(float foragingFortuneAmount) {
        super(new ItemAbility(), AbilityTriggerType.PLAYER_BREAK_SWEEP_BLOCK);
        this.foragingFortuneAmount = foragingFortuneAmount;
    }

    @Override
    public boolean conditionsMet(AbilityContext context) {
        return context.get(AbilityContextKey.PLAYER)
                .map(player -> player.getData(UnshatteredAttachments.PLAYER_REGION.get()).regionKey() == UnshatteredRegions.PLAINS_REGION)
                .orElse(false);
    }

    @Override
    public List<UnshatteredUtils.AttributeGroup> attributeBonuses(AbilityContext context) {
        return List.of(new UnshatteredUtils.AttributeGroup(UnshatteredAttributeValues.FORAGING_FORTUNE.holder, new AttributeModifier(id(), foragingFortuneAmount, AttributeModifier.Operation.ADD_VALUE)));
    }
}
