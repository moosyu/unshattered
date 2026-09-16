package io.github.moosyu.packets.handlers;

import io.github.moosyu.gui.menus.StorageMenu;
import io.github.moosyu.packets.UpdateStorageScrollPacket;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class UpdateStorageScrollHandler {
    public static void handleData(final UpdateStorageScrollPacket data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player().containerMenu instanceof StorageMenu storageMenu) {
                storageMenu.setScrollRows(data.row());
            }
        });
    }
}
