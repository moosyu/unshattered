package io.github.moosyu.events;

import io.github.moosyu.util.CollectionUtil;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;

import java.util.List;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class LivingDropHandler {
    @SubscribeEvent
    public static void onLivingDrop(LivingDropsEvent event) {
        if (event.getSource().getEntity() instanceof Player player && !player.level().isClientSide()) {
            // why are block drops in lists but living drops are in a collection?? do we have the data on this? <-- just gonna leave this there because what was i even talking about lists are collections??
            List<ItemEntity> drops = event.getDrops().stream().toList();
            for (ItemEntity drop : drops) {
                CollectionUtil.givePlayerHarvestedItemStack(player, drop.getItem());
            }
        }
    }
}