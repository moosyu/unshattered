package io.github.moosyu.items.talismans;

import io.github.moosyu.abilities.AbilityContext;
import io.github.moosyu.abilities.AbilityContextKey;
import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.abilities.PassiveAbilityItem;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.attachments.PlayerStateAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;
import java.util.Set;

public class BatTalismanBase extends TieredTalisman implements PassiveAbilityItem {
    private final float lifeStealAmount;

    public BatTalismanBase(Properties properties, float lifeStealAmount) {
        super(properties, UnshatteredUtils.getUnshatteredIdentifier("bat_talisman_path"));

        this.lifeStealAmount = lifeStealAmount;
    }

    @Override
    public void onAbilityTriggered(AbilityContext context) {
        Optional<LivingEntity> target = context.get(AbilityContextKey.TARGET);
        Optional<ServerPlayer> player = context.get(AbilityContextKey.PLAYER);

        if (target.isPresent() && target.get().isDeadOrDying() && player.isPresent()) {
            player.get().getData(UnshatteredAttachments.PLAYER_STATE).increaseStatValue(PlayerStateAttachment.Stat.HEALTH,
                    UnshatteredUtils.getDefaultAttributes(target.get()).map(supplier -> supplier.getBaseValue(UnshatteredAttributeValues.HEALTH.holder)).orElse(0.0) * lifeStealAmount,
                    player.get()
            );
        }
    }

    @Override
    public Set<AbilityTriggerType> triggerTypes() {
        return Set.of(AbilityTriggerType.ENTITY_DEATH);
    }
}
