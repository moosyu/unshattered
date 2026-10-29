package io.github.moosyu.items.attachments.sinkers;

import io.github.moosyu.abilities.AbilityContext;
import io.github.moosyu.abilities.AbilityContextKey;
import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.abilities.PassiveAbilityItem;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Set;

public final class PrismarineSinker extends Item implements PassiveAbilityItem {
    public PrismarineSinker(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public Set<AbilityTriggerType> triggerTypes() {
        return Set.of(AbilityTriggerType.PLAYER_FISH);
    }

    @Override
    public void onAbilityTriggered(AbilityContext context) {
        context.get(AbilityContextKey.PLAYER).ifPresent(player -> {
            UnshatteredUtils.addItemStackToInventory(player, new ItemStack(Items.PRISMARINE_CRYSTALS, player.getRandom().nextIntBetweenInclusive(1, 3)));
            UnshatteredUtils.addItemStackToInventory(player, new ItemStack(Items.PRISMARINE_SHARD, player.getRandom().nextIntBetweenInclusive(1, 3)));
        });
    }
}
