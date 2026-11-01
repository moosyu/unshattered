package io.github.moosyu.abilities.data;

import io.github.moosyu.abilities.*;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.List;

public class DrillEngineBonus extends Ability {
    private final float miningFortune;
    private final float miningSpeed;
    private Identifier miningSpeedIdentifier;
    private Identifier miningFortuneIdentifier;

    public DrillEngineBonus(float miningSpeed, float miningFortune) {
        super(new ItemAbility(), AbilityTriggerType.ONGOING);
        this.miningSpeed = miningSpeed;
        this.miningFortune = miningFortune;
    }

    private Identifier getSpeedIdentifier() {
        if (miningSpeedIdentifier == null) {
            miningSpeedIdentifier = UnshatteredUtils.getUnshatteredIdentifier(id().getPath() + "_mining_speed");
        }

        return miningSpeedIdentifier;
    }

    private Identifier getFortuneIdentifier() {
        if (miningFortuneIdentifier == null) {
            miningFortuneIdentifier = UnshatteredUtils.getUnshatteredIdentifier(id().getPath() + "_mining_fortune");
        }

        return miningFortuneIdentifier;
    }

    @Override
    public List<UnshatteredUtils.AttributeGroup> attributeBonuses(AbilityContext context) {
        return List.of(new UnshatteredUtils.AttributeGroup(UnshatteredAttributeValues.MINING_SPEED.holder, new AttributeModifier(getSpeedIdentifier(), miningSpeed, AttributeModifier.Operation.ADD_VALUE)),
                new UnshatteredUtils.AttributeGroup(UnshatteredAttributeValues.MINING_FORTUNE.holder, new AttributeModifier(getFortuneIdentifier(), miningFortune, AttributeModifier.Operation.ADD_VALUE))
        );
    }
}