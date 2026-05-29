package io.github.moosyu.events;

import io.github.moosyu.attachments.PlayerStateAttachment;
import io.github.moosyu.attributes.ModAttributes;
import io.github.moosyu.registers.AttributesRegistry;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.registers.AttachmentRegistry.PLAYER_STATE;

// triggers when the player starts the game or switches world
// however when the game starts this gives you the wrong value. idk why, maybe attributes arent properly loaded yet so you dont get modifiers.
// todo: fix whatever causes that
public class PlayerJoinHandler {
    @EventBusSubscriber(modid = MODID)
    public static class EventHandler {
        @SubscribeEvent
        public static void onPlayerJoin(EntityJoinLevelEvent event) {
            if (event.getEntity() instanceof Player player && !player.level().isClientSide) {
                var stats = player.getData(PLAYER_STATE.get());
                final double MAX_HEALTH = player.getAttribute(ModAttributes.HEALTH.holder).getValue();
                stats.setCurrentStat(PlayerStateAttachment.Stat.HEALTH, MAX_HEALTH);
                player.syncData(PLAYER_STATE);
            }
        }
    }
}
