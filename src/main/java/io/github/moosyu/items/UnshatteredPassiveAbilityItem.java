package io.github.moosyu.items;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public interface UnshatteredPassiveAbilityItem {
    void onAbilityTriggered(Player player, LivingEntity target);
    boolean abilityConditionsMet(Player player, LivingEntity target);
}
