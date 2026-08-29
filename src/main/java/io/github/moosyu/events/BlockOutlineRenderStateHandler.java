package io.github.moosyu.events;

import io.github.moosyu.data.UnshatteredDataMaps;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ExtractBlockOutlineRenderStateEvent;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class BlockOutlineRenderStateHandler {
    @SubscribeEvent
    public static void onRenderHighlight(ExtractBlockOutlineRenderStateEvent event) {
        event.setCanceled(event.getBlockState().getData(UnshatteredDataMaps.BREAKABLE_DROPS_DATA) == null);
    }
}
