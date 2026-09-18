package io.github.moosyu.items.talismans;

import io.github.moosyu.abilities.AbilityContext;
import io.github.moosyu.abilities.AbilityContextKey;
import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.abilities.PassiveAbilityItem;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.jspecify.annotations.Nullable;

import java.util.Set;

public class CoinTalisman extends TalismanItem implements PassiveAbilityItem {
    public CoinTalisman(Properties properties) {
        super(properties.component(UnshatteredDataComponents.DESCRIPTION.get(), true)
                .component(UnshatteredDataComponents.SELL_VALUE.get(), 70)
                .component(UnshatteredDataComponents.ABILITY.get(), new ItemAbility(UnshatteredUtils.getUnshatteredIdentifier("coin_talisman_accrual"),
                        0,
                        0,
                        0,
                        true)
                )
        );
    }

    @Override
    public void onAbilityTriggered(AbilityContext context) {
        context.get(AbilityContextKey.PLAYER).ifPresent(player -> {
            player.getData(UnshatteredAttachments.PLAYER_CURRENCY).addCoins(player.getRandom().nextIntBetweenInclusive(1, 9));
            player.syncData(UnshatteredAttachments.PLAYER_CURRENCY);
        });
    }

    @Override
    public void onAbilityFinished(AbilityContext context) {}

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
