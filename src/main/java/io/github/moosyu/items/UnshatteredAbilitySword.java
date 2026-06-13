package io.github.moosyu.items;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public interface UnshatteredAbilitySword {
    void onAbilityTriggered(Player player, LivingEntity target);
    boolean abilityConditionsMet(Player player, LivingEntity target);
}
