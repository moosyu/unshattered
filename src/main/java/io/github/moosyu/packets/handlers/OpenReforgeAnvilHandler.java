package io.github.moosyu.packets.handlers;

import io.github.moosyu.gui.menus.StorageMenu;
import io.github.moosyu.packets.OpenReforgeAnvilPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class OpenReforgeAnvilHandler {
    public static void handleData(final OpenReforgeAnvilPacket data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer) {
                serverPlayer.openMenu(new SimpleMenuProvider(
                        (containerId,
                         inventory,
                         _) -> new StorageMenu(containerId,
                                inventory,
                                new SimpleContainer(3)
                        ),
                        Component.translatable("container.unshattered.reforge_anvil")
                ));
            }
        });
    }
}
