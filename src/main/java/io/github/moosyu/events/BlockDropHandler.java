package io.github.moosyu.events;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockDropsEvent;

import java.util.List;

import static io.github.moosyu.NNO.MODID;

public class BlockDropHandler {
    @EventBusSubscriber(modid = MODID)
    public static class EventHandler {
        @SubscribeEvent
        public static void onBlockDrop(BlockDropsEvent event) {
            if (event.getBreaker() instanceof Player player && !player.level().isClientSide()) {
                Inventory inventory = player.getInventory();
                List<ItemEntity> drops = event.getDrops();
                for (ItemEntity drop : drops) {
                    // this seems to kind of just work but check in later to see if it bites me in the arse
                    inventory.add(drop.getItem());
                }
            }
        }
    }
}


