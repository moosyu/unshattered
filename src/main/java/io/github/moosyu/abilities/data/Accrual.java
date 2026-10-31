package io.github.moosyu.abilities.data;

import io.github.moosyu.abilities.Ability;
import io.github.moosyu.abilities.AbilityContext;
import io.github.moosyu.abilities.AbilityContextKey;
import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.components.ItemAbility;

public class Accrual extends Ability {
    public Accrual() {
        super(new ItemAbility(), AbilityTriggerType.TICKED);
    }

    @Override
    public boolean conditionsMet(AbilityContext context) {
        return context.get(AbilityContextKey.PLAYER).map(player -> player.getRandom().nextInt(2400) == 0).orElse(false);
    }

    @Override
    public void onTrigger(AbilityContext context) {
        context.get(AbilityContextKey.PLAYER).ifPresent(player -> {
            player.getData(UnshatteredAttachments.PLAYER_CURRENCY).addCoins(player.getRandom().nextIntBetweenInclusive(1, 9));
            player.syncData(UnshatteredAttachments.PLAYER_CURRENCY);
        });
    }
}
