package io.github.moosyu.events;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;

import java.util.List;

import static io.github.moosyu.Unshattered.MODID;

public class LivingDropHandler {
    @EventBusSubscriber(modid = MODID)
    public static class EventHandler {
        @SubscribeEvent
        public static void onLivingDrop(LivingDropsEvent event) {
            if (event.getSource().getEntity() instanceof Player player && !player.level().isClientSide()) {
                Inventory inventory = player.getInventory();
                // why are block drops in lists but living drops are in a collection?? do we have the data on this?
                List<ItemEntity> drops = event.getDrops().stream().toList();
                for (ItemEntity drop : drops) {
                    inventory.add(drop.getItem());
                }
            }
        }
    }
}
