package io.github.moosyu.events;

import io.github.moosyu.attachments.PlayerStatsAttachment;
import io.github.moosyu.registers.AttachmentRegistry;
import io.github.moosyu.registers.AttributesRegistry;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

import static io.github.moosyu.NNO.MODID;

public class PlayerJoinHandler {
    @EventBusSubscriber(modid = MODID)
    public static class EventHandler {
        @SubscribeEvent
        public static void onPlayerJoin(EntityJoinLevelEvent event) {
            if (event.getEntity() instanceof Player player) {
                var stats = player.getData(AttachmentRegistry.PLAYER_STATS.get());
                double maxHealth = player.getAttributeValue(AttributesRegistry.HEALTH);

                if (stats.getCurrentStat(PlayerStatsAttachment.Stat.HEALTH) <= 0) {
                    stats.setCurrentStat(PlayerStatsAttachment.Stat.HEALTH, maxHealth);
                }
            };
        }
    }
}
