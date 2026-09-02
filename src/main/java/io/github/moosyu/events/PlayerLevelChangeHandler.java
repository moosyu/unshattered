package io.github.moosyu.events;

import io.github.moosyu.data.attachments.PlayerStateAttachment;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.regen.RegenClientCache;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.data.attachments.UnshatteredAttachments.PLAYER_STATE;

// triggers when the player starts the game or switches world
// however when the game starts this gives you the wrong value. idk why, maybe attributes arent properly loaded yet so you dont get modifiers.
// todo: fix whatever causes that
@EventBusSubscriber(modid = MODID)
public class PlayerLevelChangeHandler {
    @SubscribeEvent
    public static void onPlayerJoin(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Player player && !player.level().isClientSide()) {
            PlayerStateAttachment stats = player.getData(PLAYER_STATE.get());
            final AttributeInstance healthAttribute = player.getAttribute(UnshatteredAttributeValues.HEALTH.holder);
            final AttributeInstance manaAttribute = player.getAttribute(UnshatteredAttributeValues.MANA.holder);
            if (healthAttribute == null || manaAttribute == null) return;
            stats.setCurrentStat(PlayerStateAttachment.Stat.HEALTH, healthAttribute.getValue(), player);
            stats.setCurrentStat(PlayerStateAttachment.Stat.MANA, manaAttribute.getValue(), player);
        }
    }

    @SubscribeEvent
    public static void onPlayerLeave(EntityLeaveLevelEvent event) {
        if (event.getEntity() instanceof Player player && player.level().isClientSide()) {
            RegenClientCache.clear();
        }
    }
}
