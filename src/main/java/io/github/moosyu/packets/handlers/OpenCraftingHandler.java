package io.github.moosyu.packets.handlers;

import io.github.moosyu.gui.menus.UnshatteredCraftingMenu;
import io.github.moosyu.packets.OpenCraftingPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class OpenCraftingHandler {
    public static void handleData(final OpenCraftingPacket data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer) {
                serverPlayer.openMenu(new SimpleMenuProvider(
                        (containerId, inventory, _) -> new UnshatteredCraftingMenu(containerId, inventory),
                        Component.translatable("container.unshattered.crafting")
                ));
            }
        });
    }
}
