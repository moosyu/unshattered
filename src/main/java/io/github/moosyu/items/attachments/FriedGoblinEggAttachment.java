package io.github.moosyu.items.attachments;

import io.github.moosyu.abilities.*;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.ItemType;
import io.github.moosyu.items.UnshatteredRarity;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.world.item.Item;

import java.util.Optional;
import java.util.Set;

public class FriedGoblinEggAttachment extends Item implements PassiveAbilityItem {
    public FriedGoblinEggAttachment(Properties properties) {
        super(properties.component(UnshatteredDataComponents.ABILITY.get(), new ItemAbility(UnshatteredUtils.getUnshatteredIdentifier("fried_goblin_egg"),
                0,
                0,
                0,
                true))
                .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
                .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarity.RARE)
                .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemType.UPGRADE_MODULE)
                .component(UnshatteredDataComponents.SELL_VALUE.get(), 20840)
        );
    }

    @Override
    public Set<AbilityTriggerType> triggerTypes() {
        return Set.of(AbilityTriggerType.PLAYER_ATTEMPT_CONSUME_FUEL);
    }

    @Override
    public boolean abilityConditionsMet(AbilityContext context) {
        return context.get(AbilityContextKey.PLAYER).map(player -> player.getRandom().nextInt(0, 3) == 0).orElse(false);
    }

    @Override
    public Optional<AbilityTriggerResult> triggerResult() {
        return Optional.of(AbilityTriggerResult.CANCEL_EVENT);
    }
}
