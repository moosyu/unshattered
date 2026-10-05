package io.github.moosyu.items.talismans;

import io.github.moosyu.abilities.AbilityContext;
import io.github.moosyu.abilities.AbilityContextKey;
import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.attachments.PlayerStateAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.abilities.PassiveAbilityItem;
import io.github.moosyu.items.UnshatteredRarity;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;
import java.util.Set;

public class BatTalisman extends TalismanItem implements PassiveAbilityItem {
    public BatTalisman(Properties properties) {
        super(properties);
    }

    @Override
    public void onAbilityTriggered(AbilityContext context) {
        Optional<LivingEntity> target = context.get(AbilityContextKey.TARGET);
        Optional<ServerPlayer> player = context.get(AbilityContextKey.PLAYER);

        if (target.isPresent() && target.get().isDeadOrDying() && player.isPresent()) {
            player.get().getData(UnshatteredAttachments.PLAYER_STATE).increaseStatValue(PlayerStateAttachment.Stat.HEALTH,
                    UnshatteredUtils.getDefaultAttributes(target.get()).map(supplier -> supplier.getBaseValue(UnshatteredAttributeValues.HEALTH.holder)).orElse(0.0) * 0.05,
                    player.get()
            );
        }
    }

    @Override
    public Set<AbilityTriggerType> triggerTypes() {
        return Set.of(AbilityTriggerType.ENTITY_DEATH);
    }
}
