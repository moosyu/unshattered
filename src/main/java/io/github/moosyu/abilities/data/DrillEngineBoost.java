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
    private final Identifier miningSpeedIdentifier;
    private final Identifier miningFortuneIdentifier;

    public DrillEngineBoost(float miningSpeed, float miningFortune) {
        super(new ItemAbility(), AbilityTriggerType.ONGOING);

        this.miningSpeed = miningSpeed;
        this.miningFortune = miningFortune;
        this.miningSpeedIdentifier = UnshatteredUtils.getUnshatteredIdentifier(identifier().getPath() + "_mining_speed");
        this.miningFortuneIdentifier = UnshatteredUtils.getUnshatteredIdentifier(identifier().getPath() + "_mining_fortune");
    }

    @Override
    public void onTrigger(AbilityContext context) {
        context.get(AbilityContextKey.PLAYER).ifPresent(player -> {
            AttributeInstance miningSpeedAttribute = player.getAttribute(UnshatteredAttributeValues.MINING_SPEED.holder);
            AttributeInstance miningFortuneAttribute = player.getAttribute(UnshatteredAttributeValues.MINING_FORTUNE.holder);

            if (miningFortuneAttribute == null || miningSpeedAttribute == null) {
                return;
            }

            miningSpeedAttribute.addTransientModifier(new AttributeModifier(miningSpeedIdentifier, miningSpeed, AttributeModifier.Operation.ADD_VALUE));
            miningFortuneAttribute.addTransientModifier(new AttributeModifier(miningFortuneIdentifier, miningFortune, AttributeModifier.Operation.ADD_VALUE));
        });
    }

    @Override
    public void onFinish(AbilityContext context) {
        context.get(AbilityContextKey.PLAYER).ifPresent(player -> {
            AttributeInstance miningSpeedAttribute = player.getAttribute(UnshatteredAttributeValues.MINING_SPEED.holder);
            AttributeInstance miningFortuneAttribute = player.getAttribute(UnshatteredAttributeValues.MINING_FORTUNE.holder);

            if (miningFortuneAttribute == null || miningSpeedAttribute == null) {
                return;
            }

            miningSpeedAttribute.removeModifier(miningSpeedIdentifier);
            miningFortuneAttribute.removeModifier(miningFortuneIdentifier);
        });
    }
}
