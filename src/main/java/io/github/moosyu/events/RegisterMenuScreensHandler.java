package io.github.moosyu.events;

import io.github.moosyu.gui.menus.UnshatteredMenus;
import io.github.moosyu.gui.screens.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import java.util.List;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class RegisterMenuScreensHandler {
    @SubscribeEvent
    public static void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(UnshatteredMenus.TALISMAN_MENU_TYPE.get(), TalismansScreen::new);
        // event.register(UnshatteredMenus.INVENTORY_MENU.get(), UnshatteredInventoryScreen::new);
        event.register(UnshatteredMenus.STORAGE_MENU_TYPE.get(), StorageScreen::new);
        event.register(UnshatteredMenus.REFORGE_ANVIL_MENU_TYPE.get(), ReforgeAnvilScreen::new);
        event.register(UnshatteredMenus.DRILL_ATTACHMENT_MENU_TYPE.get(), DrillAttachmentScreen::new);
        event.register(UnshatteredMenus.STORE_MENU_TYPE.get(), StoreScreen::new);
        event.register(UnshatteredMenus.FORGE_MENU_TYPE.get(), ForgeScreen::new);
    }
}