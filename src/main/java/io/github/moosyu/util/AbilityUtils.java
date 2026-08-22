package io.github.moosyu.util;

import io.github.moosyu.Unshattered;
import io.github.moosyu.items.UnshatteredPassiveAbilityItem;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

import javax.annotation.Nullable;
import java.util.Optional;

public class AbilityUtils {
    /**
     * @param player player having the ability triggered
     * @param target the (optional) target of the ability, obviously if its something like increasing foraging fortune the target is null
     * @param triggeringItem the item that's possibly triggering the passive, might be needed sometimes to make sure the finish isn't run for the wrong item but generally both will be getting run on the same thread
     * @return the ability item or null if the item doesnt have a passive ability
     */
    public static UnshatteredPassiveAbilityItem triggerPassiveAbility(Player player, @Nullable LivingEntity target, @Nullable Item triggeringItem) {
        if (triggeringItem instanceof UnshatteredPassiveAbilityItem passiveAbilityItem && passiveAbilityItem.abilityConditionsMet(player, target)) {
            passiveAbilityItem.onAbilityTriggered(player, target);
            return passiveAbilityItem;
        }
        return null;
    }

    /**
     * finishes a passive ability, triggering onAbilityFinished which should reset everything
     * @param player player having the ability finished
     * @param target the (optional) target of the ability, obviously if its something like increasing foraging fortune the target is null
     * @param triggeringItem the item that a passive was triggered for, it's checked if it's null inside before trying to finish so no need to check
     */
    public static void finishPassiveAbility(Player player, @Nullable LivingEntity target, @Nullable UnshatteredPassiveAbilityItem triggeringItem) {
        if (triggeringItem != null) {
            triggeringItem.onAbilityFinished(player, target);
        }
    }

    /**
     * @param player player having the attribute modified/checked
     * @param attribute the attribute to get instance of
     * @return an optional attribute instance of the selected attribute to modify
     */
    public static Optional<AttributeInstance> getAttributeInstance(Player player, Holder<Attribute> attribute) {
        AttributeInstance attributeInstance = player.getAttribute(attribute);
        if (attributeInstance == null) {
            Unshattered.LOGGER.error("{} is null (from getAttributeInstance)", attribute.getRegisteredName());
            return Optional.empty();
        }
        return Optional.of(attributeInstance);
    }
}
