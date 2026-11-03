package io.github.moosyu.abilities.data;

import io.github.moosyu.abilities.Ability;
import io.github.moosyu.abilities.AbilityContext;
import io.github.moosyu.abilities.AbilityContextKey;
import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.ArrayList;
import java.util.List;

public class Greed extends Ability {
    private static final double SCALE = 0.5;
    private static final double EXPONENT = 0.25;

    public Greed() {
        super(new ItemAbility(), AbilityTriggerType.ONGOING);
    }

    @Override
    public void onTrigger(AbilityContext context) {
        context.get(AbilityContextKey.PLAYER).ifPresent(player -> UnshatteredUtils.getAttributeInstance(
                player, UnshatteredAttributeValues.FINAL_DAMAGE_MODIFIER.holder).ifPresent(attribute -> {
                    double coins = player.getData(UnshatteredAttachments.PLAYER_CURRENCY.get()).getCoins();
                    attribute.addOrUpdateTransientModifier(new AttributeModifier(
                            id(),
                            SCALE * Math.pow(coins, EXPONENT),
                            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    );
                })
        );
    }

    @Override
    public void onFinish(AbilityContext context) {
        context.get(AbilityContextKey.PLAYER)
                .flatMap(player -> UnshatteredUtils.getAttributeInstance(player, UnshatteredAttributeValues.FINAL_DAMAGE_MODIFIER.holder))
                .ifPresent(attribute -> attribute.removeModifier(id()));
    }

    @Override
    public List<UnshatteredUtils.AttributeGroup> attributeBonuses(AbilityContext context) {
        List<UnshatteredUtils.AttributeGroup> bonuses = new ArrayList<>();
        context.get(AbilityContextKey.PLAYER).ifPresent(player -> {
            double coins = player.getData(UnshatteredAttachments.PLAYER_CURRENCY.get()).getCoins();
            bonuses.add(new UnshatteredUtils.AttributeGroup(UnshatteredAttributeValues.FINAL_DAMAGE_MODIFIER.holder, new AttributeModifier(
                    id(),
                    SCALE * Math.pow(coins, EXPONENT),
                    AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            ));
        });

        return bonuses;
    }
}