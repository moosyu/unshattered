package io.github.moosyu.abilities.data;

import io.github.moosyu.abilities.Ability;
import io.github.moosyu.abilities.AbilityContext;
import io.github.moosyu.abilities.AbilityContextKey;
import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.blocks.UnshatteredBlocks;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.Optional;

public class TitaniumFanatic extends Ability {
    private final int fortuneBoost;

    public TitaniumFanatic(int fortuneBoost) {
        super(new ItemAbility(), AbilityTriggerType.PLAYER_BREAK_MINING_BLOCK);

        this.fortuneBoost = fortuneBoost;
    }

    @Override
    public boolean conditionsMet(AbilityContext context) {
        Optional<ServerPlayer> player = context.get(AbilityContextKey.PLAYER);
        return player.map(
                serverPlayer -> UnshatteredUtils.getLookedAtBlock(serverPlayer, serverPlayer.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE))
                        .map(
                                blockHitResult -> serverPlayer.level()
                                        .getBlockState(blockHitResult.getBlockPos())
                                        .is(UnshatteredBlocks.BREAKABLE_TITANIUM_BLOCK)
                        ).orElse(false)
        ).orElse(false);
    }

    @Override
    public void onTrigger(AbilityContext context) {
        context.get(AbilityContextKey.PLAYER).flatMap(player -> UnshatteredUtils.getAttributeInstance(player,
                UnshatteredAttributeValues.MINING_FORTUNE.holder)
        ).ifPresent(attribute -> attribute.addTransientModifier(new AttributeModifier(identifier(), fortuneBoost, AttributeModifier.Operation.ADD_VALUE)));
    }

    @Override
    public void onFinish(AbilityContext context) {
        context.get(AbilityContextKey.PLAYER).flatMap(player -> UnshatteredUtils.getAttributeInstance(player,
                UnshatteredAttributeValues.MINING_FORTUNE.holder)
        ).ifPresent(attribute -> attribute.removeModifier(identifier()));
    }
}
