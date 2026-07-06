package io.github.moosyu.events;

import io.github.moosyu.Unshattered;
import io.github.moosyu.gui.screens.ProfileScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class ScreenEventHandler {
    @SubscribeEvent
    public static void onScreenEventOpen(ScreenEvent.Opening event) {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            Unshattered.LOGGER.error("player is null... (from screen opening)");
            return;
        }
        // override inventory
        if (event.getNewScreen() instanceof InventoryScreen && !player.isCreative()) {
            event.setNewScreen(ProfileScreen.Tabs.INVENTORY.createScreen.apply(player));
        }
    }
}
