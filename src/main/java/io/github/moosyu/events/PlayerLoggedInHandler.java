package io.github.moosyu.events;

import io.github.moosyu.attachments.PlayerSkillsAttachment;
import io.github.moosyu.attachments.PlayerStatsAttachment;
import io.github.moosyu.registers.AttachmentRegistry;
import io.github.moosyu.registers.AttributesRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import static io.github.moosyu.NNO.MODID;

public class PlayerLoggedInHandler {
    @EventBusSubscriber(modid = MODID)
    public static class EventHandler {
        @SubscribeEvent
        public static void onPlayerClone(PlayerEvent.PlayerLoggedInEvent event) {
            if (event.getEntity() instanceof ServerPlayer player) {
                player.getData(AttachmentRegistry.PLAYER_STATS.get()).setCurrentStat(PlayerStatsAttachment.Stat.HEALTH, player.getAttribute(AttributesRegistry.HEALTH).getValue());
            }
        }
    }
}
