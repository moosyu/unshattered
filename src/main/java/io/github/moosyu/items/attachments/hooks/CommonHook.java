package io.github.moosyu.items.attachments.hooks;

import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.abilities.PassiveAbilityItem;
import net.minecraft.world.item.Item;

import java.util.Set;

public class CommonHook extends Item implements PassiveAbilityItem {
    public CommonHook(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public Set<AbilityTriggerType> triggerTypes() {
        return Set.of(AbilityTriggerType.PLAYER_FISH);
    }
}
