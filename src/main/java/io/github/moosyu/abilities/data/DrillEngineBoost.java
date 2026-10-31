package io.github.moosyu.abilities.data;

import io.github.moosyu.abilities.*;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class DrillEngineBoost extends Ability {
    private final float miningFortune;
    private final float miningSpeed;
    private Identifier miningSpeedIdentifier;
    private Identifier miningFortuneIdentifier;

    public DrillEngineBoost(float miningSpeed, float miningFortune) {
        super(new ItemAbility(), AbilityTriggerType.ONGOING);
        this.miningSpeed = miningSpeed;
        this.miningFortune = miningFortune;
    }

    private Identifier getSpeedIdentifier() {
        if (miningSpeedIdentifier == null) {
            miningSpeedIdentifier = UnshatteredUtils.getUnshatteredIdentifier(identifier().getPath() + "_mining_speed");
        }
        return miningSpeedIdentifier;
    }

    private Identifier getFortuneIdentifier() {
        if (miningFortuneIdentifier == null) {
            miningFortuneIdentifier = UnshatteredUtils.getUnshatteredIdentifier(identifier().getPath() + "_mining_fortune");
        }
        return miningFortuneIdentifier;
    }

    @Override
    public void onTrigger(AbilityContext context) {
        context.get(AbilityContextKey.PLAYER).ifPresent(player -> {
            AttributeInstance speedAttribute = player.getAttribute(UnshatteredAttributeValues.MINING_SPEED.holder);
            AttributeInstance fortuneAttribute = player.getAttribute(UnshatteredAttributeValues.MINING_FORTUNE.holder);
            if (speedAttribute == null || fortuneAttribute == null) return;

            speedAttribute.addTransientModifier(new AttributeModifier(getSpeedIdentifier(), miningSpeed, AttributeModifier.Operation.ADD_VALUE));
            fortuneAttribute.addTransientModifier(new AttributeModifier(getFortuneIdentifier(), miningFortune, AttributeModifier.Operation.ADD_VALUE));
        });
    }

    @Override
    public void onFinish(AbilityContext context) {
        context.get(AbilityContextKey.PLAYER).ifPresent(player -> {
            AttributeInstance speedAttribute = player.getAttribute(UnshatteredAttributeValues.MINING_SPEED.holder);
            AttributeInstance fortuneAttribute = player.getAttribute(UnshatteredAttributeValues.MINING_FORTUNE.holder);
            if (speedAttribute == null || fortuneAttribute == null) return;

            speedAttribute.removeModifier(getSpeedIdentifier());
            fortuneAttribute.removeModifier(getFortuneIdentifier());
        });
    }
}