package io.github.moosyu.items.attachments;

import io.github.moosyu.abilities.AbilityContext;
import io.github.moosyu.abilities.AbilityContextKey;
import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.abilities.PassiveAbilityItem;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.ItemType;
import io.github.moosyu.items.UnshatteredRarity;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;

import java.util.Set;

public class DrillEngineAttachment extends Item implements PassiveAbilityItem {
    private final int miningSpeed;
    private final int miningFortune;
    private final Identifier miningSpeedIdentifier;
    private final Identifier miningFortuneIdentifier;

    public DrillEngineAttachment(Properties properties, int miningSpeed, int miningFortune, String identifier, int sellPrice, UnshatteredRarity rarity) {
        super(properties.stacksTo(1)
                .component(UnshatteredDataComponents.SELL_VALUE.get(), sellPrice)
                .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemType.DRILL_ENGINE)
                .component(UnshatteredDataComponents.RARITY.get(), rarity)
                .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
                .component(UnshatteredDataComponents.ABILITY.get(), new ItemAbility(UnshatteredUtils.getUnshatteredIdentifier(identifier),
                        0,
                        0,
                        0,
                        true
                        )
                )
        );

        this.miningSpeed = miningSpeed;
        this.miningFortune = miningFortune;
        miningSpeedIdentifier = UnshatteredUtils.getUnshatteredIdentifier(identifier + "_mining_speed");
        miningFortuneIdentifier = UnshatteredUtils.getUnshatteredIdentifier(identifier + "_mining_fortune");
    }

    @Override
    public Set<AbilityTriggerType> triggerTypes() {
        return Set.of(AbilityTriggerType.ONGOING);
    }

    @Override
    public void onAbilityTriggered(AbilityContext context) {
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
    public void onAbilityFinished(AbilityContext context) {
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
