package io.github.moosyu.events;

import io.github.moosyu.attachments.PlayerStateAttachment;
import io.github.moosyu.attributes.UnshatteredAttributes;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.attachments.AttachmentRegistry.PLAYER_STATE;

// triggers when the player starts the game or switches world
// however when the game starts this gives you the wrong value. idk why, maybe attributes arent properly loaded yet so you dont get modifiers.
// todo: fix whatever causes that
@EventBusSubscriber(modid = MODID)
public class PlayerJoinHandler {
    @SubscribeEvent
    public static void onPlayerJoin(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Player player && !player.level().isClientSide()) {
            var stats = player.getData(PLAYER_STATE.get());
            final AttributeInstance healthAttribute = player.getAttribute(UnshatteredAttributes.HEALTH.holder);
            final AttributeInstance manaAttribute = player.getAttribute(UnshatteredAttributes.MANA.holder);
            if (healthAttribute == null || manaAttribute == null) return;
            stats.setCurrentStat(PlayerStateAttachment.Stat.HEALTH, healthAttribute.getValue());
            player.syncData(PLAYER_STATE);
            stats.setCurrentStat(PlayerStateAttachment.Stat.MANA, manaAttribute.getValue());
            player.syncData(PLAYER_STATE);
        }
    }
}
