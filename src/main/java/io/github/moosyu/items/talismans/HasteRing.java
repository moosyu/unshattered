package io.github.moosyu.items.talismans;

import io.github.moosyu.abilities.AbilityContext;
import io.github.moosyu.abilities.AbilityContextKey;
import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.abilities.PassiveAbilityItem;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.rarities.UnshatteredRarities;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.Set;

public class HasteRing extends TalismanItem implements PassiveAbilityItem {
    private static final Identifier ABILITY_IDENTIFIER = UnshatteredUtils.getUnshatteredIdentifier("heavy_strike");

    public HasteRing(Properties properties) {
        super(properties.component(UnshatteredDataComponents.ABILITY.get(), new ItemAbility(ABILITY_IDENTIFIER, 0, 0, 0, true))
                .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.UNCOMMON)
                .component(UnshatteredDataComponents.SELL_VALUE.get(), 80000)
                .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
        );
    }

    @Override
    public void onAbilityTriggered(AbilityContext context) {
        context.get(AbilityContextKey.PLAYER).ifPresent(player -> {
            AttributeInstance strength = player.getAttribute(UnshatteredAttributeValues.STRENGTH.holder);
            AttributeInstance miningSpeed = player.getAttribute(UnshatteredAttributeValues.MINING_SPEED.holder);
            if (miningSpeed != null && strength != null) {
                AttributeModifier attributeModifier = miningSpeed.getModifier(ABILITY_IDENTIFIER);
                int bonusMiningSpeed = ((int) strength.getValue() / 20) * 4;
                if (attributeModifier != null) {
                    if (attributeModifier.amount() != bonusMiningSpeed) {
                        miningSpeed.removeModifier(ABILITY_IDENTIFIER);
                        miningSpeed.addTransientModifier(new AttributeModifier(ABILITY_IDENTIFIER, bonusMiningSpeed, AttributeModifier.Operation.ADD_VALUE));
                    }
                } else {
                    miningSpeed.addTransientModifier(new AttributeModifier(ABILITY_IDENTIFIER, bonusMiningSpeed, AttributeModifier.Operation.ADD_VALUE));
                }
            }
        });
    }

    @Override
    public void onAbilityFinished(AbilityContext context) {}

    @Override
    public boolean abilityConditionsMet(AbilityContext context) {
        // check occurs in trigger
        return true;
    }

    @Override
    public Set<AbilityTriggerType> triggerTypes() {
        return Set.of(AbilityTriggerType.ONGOING, AbilityTriggerType.TICKED);
    }
}
