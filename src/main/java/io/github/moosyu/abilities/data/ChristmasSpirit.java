package io.github.moosyu.abilities.data;

import io.github.moosyu.abilities.Ability;
import io.github.moosyu.abilities.AbilityContext;
import io.github.moosyu.abilities.AbilityContextKey;
import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.attributes.UnshatteredAttributeValue;
import io.github.moosyu.data.UnshatteredStatistics;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.List;

public class ChristmasSpirit extends Ability {
    public ChristmasSpirit() {
        super(new ItemAbility(), AbilityTriggerType.ONGOING);
    }

    @Override
    public List<UnshatteredUtils.AttributeGroup> attributeBonuses(AbilityContext context) {
        return context.get(AbilityContextKey.PLAYER).map(player -> {
            AttributeInstance health = player.getAttribute(UnshatteredAttributeValue.MAX_HEALTH.holder);
            if (health == null) {
                return List.<UnshatteredUtils.AttributeGroup>of();
            }

            float giftsOpened = 0;
            if (player.level().isClientSide()) {
                if (player instanceof LocalPlayer localPlayer) {
                    giftsOpened = localPlayer.getStats().getValue(Stats.CUSTOM.get(UnshatteredStatistics.GIFTS_OPENED.get()));
                }
            } else {
                if (player instanceof ServerPlayer serverPlayer) {
                    giftsOpened = serverPlayer.getStats().getValue(Stats.CUSTOM.get(UnshatteredStatistics.GIFTS_OPENED.get()));
                }
            }

            return List.of(new UnshatteredUtils.AttributeGroup(
                    UnshatteredAttributeValue.MAX_HEALTH.holder,
                    new AttributeModifier(id(), (int) (giftsOpened / 50), AttributeModifier.Operation.ADD_VALUE))
            );
        }).orElse(List.of());
    }
}
