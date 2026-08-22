package io.github.moosyu.items;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

// only has code to make this do stuff if its for killing an enemy or during tree sweep, more will be added when required
// not really passive as it only changes something the instant that the ability is to be triggered not always
public interface UnshatteredPassiveAbilityItem {
    /**
     * runs when a passive ability should be fired off
     * @param player player triggering passive
     * @param target target that is possibly required for the ability
     */
    void onAbilityTriggered(Player player, @Nullable LivingEntity target);

    /**
     * runs when a passive ability should be ended, should be used to reset things like attribute modifiers
     * @param player player that triggered the passive
     * @param target target that is possibly required for the ability
     */
    void onAbilityFinished(Player player, @Nullable LivingEntity target);

    /**
     * @param player player trying to trigger the passive
     * @param target target that is possibly required for the passive
     * @return whether or not the conditions were met to run the passive effect
     */
    boolean abilityConditionsMet(Player player, @Nullable LivingEntity target);
}
