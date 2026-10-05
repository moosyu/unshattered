package io.github.moosyu.items.talismans;

import io.github.moosyu.abilities.*;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.item.Item;

import java.util.Optional;
import java.util.Set;

public class FireTalisman extends Item implements PassiveAbilityItem {
    public FireTalisman(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public boolean abilityConditionsMet(AbilityContext context) {
        return context.get(AbilityContextKey.DAMAGE_SOURCE).map(source -> source.is(DamageTypeTags.IS_FIRE)).orElse(false);
    }

    @Override
    public Set<AbilityTriggerType> triggerTypes() {
        return Set.of(AbilityTriggerType.PLAYER_INCOMING_DAMAGE);
    }

    @Override
    public Optional<?> triggerResult() {
        return Optional.of(AbilityTriggerResult.CANCEL_EVENT);
    }
}
