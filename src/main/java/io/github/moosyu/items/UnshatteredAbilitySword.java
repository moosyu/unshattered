package io.github.moosyu.items;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public abstract class UnshatteredAbilitySword extends UnshatteredSword {
    public UnshatteredAbilitySword(Properties properties) {
        super(properties);
    }

    public abstract void onAbilityTriggered(Player player, LivingEntity target);
    public abstract boolean abilityConditionsMet(Player player, LivingEntity target);
}
