package io.github.moosyu.items.talismans;

import io.github.moosyu.abilities.*;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.UnshatteredRarity;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.resources.Identifier;

import java.util.Optional;
import java.util.Set;

public class DwarfTurtleTalisman extends TalismanItem implements PassiveAbilityItem {
    public static final Identifier ABILITY_IDENTIFIER = UnshatteredUtils.getUnshatteredIdentifier("turtle_stability");

    public DwarfTurtleTalisman(Properties properties) {
        super(properties);
    }

    @Override
    public Set<AbilityTriggerType> triggerTypes() {
        return Set.of(AbilityTriggerType.PLAYER_INCOMING_KNOCKBACK);
    }

    @Override
    public Optional<AbilityTriggerResult> triggerResult() {
        return Optional.of(AbilityTriggerResult.CANCEL_EVENT);
    }
}
