package io.github.moosyu.events;

import io.github.moosyu.attachments.PlayerStatsAttachment;
import io.github.moosyu.registers.AttributesRegistry;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;

import java.util.List;

import static io.github.moosyu.NNO.MODID;
import static io.github.moosyu.registers.AttachmentRegistry.PLAYER_STATS;

// this shit does NOT work
// seems that maxHealth value is being given before the equipment change was made
public class LivingEquipmentChangeHandler {
    @EventBusSubscriber(modid = MODID)
    public static class EventHandler {
        @SubscribeEvent
        public static void onLivingEquipmentChange(LivingEquipmentChangeEvent event) {
            if (event.getEntity() instanceof Player player && !player.level().isClientSide()) {
                var stats = player.getData(PLAYER_STATS.get());
                double currentHealth = stats.getCurrentStat(PlayerStatsAttachment.Stat.HEALTH);
                double maxHealth = player.getAttribute(AttributesRegistry.HEALTH).getValue();

                System.out.println(currentHealth);
                System.out.println(maxHealth);

                if (maxHealth < currentHealth) {
                    System.out.println("triggered");
                    stats.setCurrentStat(PlayerStatsAttachment.Stat.HEALTH, maxHealth);
                }
                player.syncData(PLAYER_STATS);
            }
        }
    }
}
