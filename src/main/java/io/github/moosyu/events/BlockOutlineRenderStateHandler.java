package io.github.moosyu.events;

import io.github.moosyu.data.UnshatteredDataMaps;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ExtractBlockOutlineRenderStateEvent;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class BlockOutlineRenderStateHandler {
    @SubscribeEvent
    public static void onRenderHighlight(ExtractBlockOutlineRenderStateEvent event) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        if (!player.isCreative()) {
            event.setCanceled(event.getBlockState().getData(UnshatteredDataMaps.BREAKABLE_DROPS_DATA) == null);
        }
    }
}
