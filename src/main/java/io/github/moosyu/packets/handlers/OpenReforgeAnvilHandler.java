package io.github.moosyu.packets.handlers;

import io.github.moosyu.gui.menus.ReforgeAnvilMenu;
import io.github.moosyu.packets.OpenReforgeAnvilPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class OpenReforgeAnvilHandler {
    public static void handleData(final OpenReforgeAnvilPacket data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer) {
                serverPlayer.openMenu(new SimpleMenuProvider(
                        (containerId,
                         inventory,
                         _) -> new ReforgeAnvilMenu(containerId,
                                inventory,
                                ContainerLevelAccess.create(serverPlayer.level(), serverPlayer.blockPosition())
                        ),
                        Component.translatable("container.unshattered.reforge_anvil")
                ));
            }
        });
    }
}
