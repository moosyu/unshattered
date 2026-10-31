package io.github.moosyu.abilities.data;

import io.github.moosyu.abilities.Ability;
import io.github.moosyu.abilities.AbilityContext;
import io.github.moosyu.abilities.AbilityContextKey;
import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.data.datagen.UnshatteredBlockTagsProvider;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.Optional;

public class MithrilMiner extends Ability {
    private final float speedBoost;

    public MithrilMiner(float speedBoost) {
        super(new ItemAbility(), AbilityTriggerType.ONGOING);

        this.speedBoost = speedBoost;
    }

    @Override
    public boolean conditionsMet(AbilityContext context) {
        Optional<ServerPlayer> player = context.get(AbilityContextKey.PLAYER);
        return player.map(
                serverPlayer ->
                        UnshatteredUtils.getLookedAtBlock(serverPlayer, serverPlayer.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE))
                                .map(blockHitResult -> serverPlayer.level().getBlockState(blockHitResult.getBlockPos()).is(UnshatteredBlockTagsProvider.MITHRIL_BLOCKS))
                                .orElse(false)
        ).orElse(false);
    }

    @Override
    public void onTrigger(AbilityContext context) {
        context.get(AbilityContextKey.PLAYER).ifPresent(player -> {
            AttributeInstance miningSpeed = player.getAttribute(UnshatteredAttributeValues.MINING_SPEED.holder);
            if (miningSpeed != null) {
                miningSpeed.addTransientModifier(new AttributeModifier(identifier(), speedBoost, AttributeModifier.Operation.ADD_VALUE));
            }
        });
    }

    @Override
    public void onFinish(AbilityContext context) {
        context.get(AbilityContextKey.PLAYER).ifPresent(player -> {
            AttributeInstance miningSpeed = player.getAttribute(UnshatteredAttributeValues.MINING_SPEED.holder);
            if (miningSpeed != null) {
                miningSpeed.removeModifier(identifier());
            }
        });
    }
}
