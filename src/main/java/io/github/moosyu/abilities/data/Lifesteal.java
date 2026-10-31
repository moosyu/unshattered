package io.github.moosyu.abilities.data;

import io.github.moosyu.abilities.*;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.attachments.PlayerStateAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;

public class Lifesteal extends Ability {
    private final float lifeStealAmount;

    public Lifesteal(float lifeStealAmount) {
        super(new ItemAbility(), AbilityTriggerType.ENTITY_DEATH);

        this.lifeStealAmount = lifeStealAmount;
    }

    @Override
    public void onTrigger(AbilityContext context) {
        Optional<LivingEntity> target = context.get(AbilityContextKey.TARGET);
        Optional<ServerPlayer> player = context.get(AbilityContextKey.PLAYER);

        if (target.isPresent() && target.get().isDeadOrDying() && player.isPresent()) {
            player.get().getData(UnshatteredAttachments.PLAYER_STATE).increaseStatValue(PlayerStateAttachment.Stat.HEALTH,
                    UnshatteredUtils.getDefaultAttributes(target.get()).map(supplier -> supplier.getBaseValue(UnshatteredAttributeValues.MAX_HEALTH.holder)).orElse(0.0) * lifeStealAmount,
                    player.get()
            );
        }
    }
}
