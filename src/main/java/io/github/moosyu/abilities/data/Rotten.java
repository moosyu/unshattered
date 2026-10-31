package io.github.moosyu.abilities.data;

import io.github.moosyu.abilities.Ability;
import io.github.moosyu.abilities.AbilityContext;
import io.github.moosyu.abilities.AbilityContextKey;
import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.phys.AABB;

public class Rotten extends Ability {
    public static int DIAMETER = 10;

    public Rotten() {
        super(new ItemAbility(), AbilityTriggerType.PLAYER_BREAK_MINING_BLOCK);
    }

    @Override
    public void onTrigger(AbilityContext context) {
        context.get(AbilityContextKey.PLAYER).ifPresent(player -> {
            AABB boundingBox = AABB.ofSize(player.position(), DIAMETER + 1, DIAMETER + 1, DIAMETER + 1);

            int fortuneBoost = player.level().getEntities(null, boundingBox).stream().filter(entity -> entity.is(EntityTypeTags.UNDEAD)).mapToInt(_ -> 5).sum();
            if (fortuneBoost > 0) {
                UnshatteredUtils.getAttributeInstance(player, UnshatteredAttributeValues.MINING_FORTUNE.holder).ifPresent(attribute ->
                        attribute.addTransientModifier(new AttributeModifier(identifier(), fortuneBoost, AttributeModifier.Operation.ADD_VALUE))
                );
            }
        });
    }

    @Override
    public void onFinish(AbilityContext context) {
        context.get(AbilityContextKey.PLAYER).flatMap(player -> UnshatteredUtils.getAttributeInstance(player,
                UnshatteredAttributeValues.MINING_FORTUNE.holder)
        ).ifPresent(attribute -> attribute.removeModifier(identifier()));
    }
}
