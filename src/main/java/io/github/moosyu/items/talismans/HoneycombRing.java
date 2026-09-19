package io.github.moosyu.items.talismans;

import io.github.moosyu.abilities.AbilityContext;
import io.github.moosyu.abilities.AbilityContextKey;
import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.abilities.PassiveAbilityItem;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.attachments.PlayerStateAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.rarities.UnshatteredRarities;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;
import java.util.Set;

public class HoneycombRing extends TalismanItem implements PassiveAbilityItem {
    public HoneycombRing(Properties properties) {
        super(properties.component(UnshatteredDataComponents.ABILITY.get(), new ItemAbility(UnshatteredUtils.getUnshatteredIdentifier("apian_aegis"), 0, 0, 0, true))
                .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.UNCOMMON)
                .component(UnshatteredDataComponents.SELL_VALUE.get(), 150000)
                .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
        );
    }

    @Override
    public void onAbilityTriggered(AbilityContext context) {
        System.out.println("honeycomb");
    }

    @Override
    public void onAbilityFinished(AbilityContext context) {}

    @Override
    public boolean abilityConditionsMet(AbilityContext context) {
        return true;
    }

    @Override
    public Set<AbilityTriggerType> triggerTypes() {
        return Set.of(AbilityTriggerType.PLAYER_TAKE_DAMAGE);
    }
}
