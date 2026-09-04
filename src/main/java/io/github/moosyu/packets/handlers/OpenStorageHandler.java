package io.github.moosyu.packets.handlers;

import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.gui.menus.StorageMenu;
import io.github.moosyu.packets.OpenStoragePacket;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class OpenStorageHandler {
    public static void handleData(final OpenStoragePacket data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer) {
                serverPlayer.openMenu(new SimpleMenuProvider(
                        (containerId,
                         inventory,
                         _) -> new StorageMenu(containerId,
                                inventory,
                                serverPlayer.getData(UnshatteredAttachments.PLAYER_BANK_STORAGE)
                        ),
                        Component.translatable("container.unshattered.storage")
                ));
            }
        });
    }
}
