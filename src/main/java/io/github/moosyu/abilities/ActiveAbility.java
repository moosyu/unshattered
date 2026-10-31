package io.github.moosyu.abilities;

import io.github.moosyu.data.attachments.PlayerAbilityEffectsAttachment;
import io.github.moosyu.data.attachments.PlayerStateAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public abstract class ActiveAbility extends Ability {
    public ActiveAbility(ItemAbility data) {
        super(data, null);
    }

    protected boolean canActivate(AbilityContext context) {
        return true;
    }

    protected abstract void onActivate(AbilityContext context, boolean refreshing);

    protected long effectDuration(AbilityContext context) {
        return data.duration();
    }

    protected boolean canStartEffect(boolean refreshing) {
        return true;
    }

    public final boolean tryActivate(ServerPlayer player, ItemStack itemStack) {
        if (player.getCooldowns().isOnCooldown(itemStack)
                || !UnshatteredUtils.passesManaCheck(player, data.manaCost())
        ) {
            return false;
        }

        AbilityContext context = new AbilityContext()
                .add(AbilityContextKey.PLAYER, player)
                .add(AbilityContextKey.ITEM_STACK, itemStack);
        if (!canActivate(context)) {
            return false;
        }

        PlayerAbilityEffectsAttachment effects = player.getData(UnshatteredAttachments.PLAYER_ABILITIES.get());
        boolean refreshing = effects.activeEffects.containsKey(identifier());

        onActivate(context, refreshing);

        long duration = effectDuration(context);
        if (duration > 0 && canStartEffect(refreshing)) {
            effects.addActiveEffect(this, duration, player.level());
        }

        if (!player.isCreative()) {
            player.getData(UnshatteredAttachments.PLAYER_STATE.get()).decreaseStatValue(PlayerStateAttachment.Stat.MANA, data.manaCost(), player);
        }

        player.getCooldowns().addCooldown(itemStack, data.cooldown());
        return true;
    }
}