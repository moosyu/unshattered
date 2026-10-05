package io.github.moosyu.items.talismans;

import io.github.moosyu.abilities.AbilityContext;
import io.github.moosyu.abilities.AbilityContextKey;
import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.abilities.PassiveAbilityItem;
import net.minecraft.world.item.Item;

import java.util.Set;

public class CoinTalisman extends Item implements PassiveAbilityItem {
    public CoinTalisman(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public void onAbilityTriggered(AbilityContext context) {
        context.get(AbilityContextKey.PLAYER).ifPresent(player -> {
            player.getData(UnshatteredAttachments.PLAYER_CURRENCY).addCoins(player.getRandom().nextIntBetweenInclusive(1, 9));
            player.syncData(UnshatteredAttachments.PLAYER_CURRENCY);
        });
    }

    @Override
    public boolean abilityConditionsMet(AbilityContext context) {
        // roughly once every two minutes should be true
        return context.get(AbilityContextKey.PLAYER).map(player -> player.getRandom().nextInt(2400) == 0).orElse(false);
    }

    @Override
    public Set<AbilityTriggerType> triggerTypes() {
        return Set.of(AbilityTriggerType.TICKED, AbilityTriggerType.ONGOING);
    }
}
