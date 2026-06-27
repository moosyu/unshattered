package io.github.moosyu.items.tools.rods;

import io.github.moosyu.Unshattered;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.UnshatteredPassiveAbilityItem;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;

public class UnshatteredRod extends FishingRodItem implements UnshatteredPassiveAbilityItem {
    public UnshatteredRod(Properties properties) {
        super(properties.component(UnshatteredDataComponents.ITEM_ABILITY.get(), new ItemAbility("fish_out_of_water", 0, 0, 0, true)));
    }

    @Override
    public void onAbilityTriggered(Player player, LivingEntity target) {
        AttributeInstance finalDamageModifierAttribute = player.getAttribute(UnshatteredAttributeValues.FINAL_DAMAGE_MODIFIER.holder);
        if (finalDamageModifierAttribute == null) {
            Unshattered.LOGGER.error("final damage modifier is null!! (from rod base)");
            return;
        }
        finalDamageModifierAttribute.setBaseValue(0.0d);
    }

    @Override
    public boolean abilityConditionsMet(Player player, LivingEntity target) {
        return !target.is(EntityTypeTags.AQUATIC);
    }
}
