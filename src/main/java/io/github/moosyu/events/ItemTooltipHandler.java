package io.github.moosyu.events;

import io.github.moosyu.helpers.TooltipHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import static io.github.moosyu.Unshattered.MODID;

public class ItemTooltipHandler {
    @EventBusSubscriber(modid = MODID)
    public static class EventHandler {
        @SubscribeEvent
        public static void onItemTooltip(ItemTooltipEvent event) {
            event.getToolTip().clear();
            TooltipHelper.displayHoverText(event.getItemStack(), event.getToolTip());
        }
    }
}
