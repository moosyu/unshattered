package io.github.moosyu.abilities.data;

import io.github.moosyu.abilities.*;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Set;

public class FishOutOfWater extends Ability {
    public FishOutOfWater() {
        super(new ItemAbility(), AbilityTriggerType.PLAYER_DEAL_DAMAGE, new AbilityResults().add(AbilityResult.CANCEL_EVENT, true));
    }

    @Override
    public boolean conditionsMet(AbilityContext context) {
        LivingEntity target = context.get(AbilityContextKey.TARGET).orElse(null);
        ItemStack rod = context.get(AbilityContextKey.ITEM_STACK).orElse(null);

        if (rod == null || target == null) {
            return true;
        } else {
            return !target.is(EntityTypeTags.AQUATIC);
        }
    }

    @Override
    public void onTrigger(AbilityContext context) {
        context.get(AbilityContextKey.PLAYER).ifPresent(player ->
                player.sendSystemMessage(Component.translatable("combat.messages.unshattered.failed").withColor(UnshatteredUtils.RED))
        );
    }
}
