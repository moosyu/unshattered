package io.github.moosyu.abilities.data;

import io.github.moosyu.abilities.Ability;
import io.github.moosyu.abilities.AbilityContext;
import io.github.moosyu.abilities.AbilityContextKey;
import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class Moskstraumen extends Ability {
    public Moskstraumen() {
        super(new ItemAbility(), AbilityTriggerType.PLAYER_FISH);
    }

    @Override
    public void onTrigger(AbilityContext context) {
        context.get(AbilityContextKey.PLAYER).ifPresent(player -> {
            UnshatteredUtils.addItemStackToInventory(player, new ItemStack(Items.PRISMARINE_CRYSTALS, player.getRandom().nextIntBetweenInclusive(1, 3)));
            UnshatteredUtils.addItemStackToInventory(player, new ItemStack(Items.PRISMARINE_SHARD, player.getRandom().nextIntBetweenInclusive(1, 3)));
        });
    }
}
