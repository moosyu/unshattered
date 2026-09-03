package io.github.moosyu.packets.handlers;

import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.gui.menus.TalismansMenu;
import io.github.moosyu.packets.OpenTalismanBagPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class OpenTalismanBagHandler {
    public static void handleData(final OpenTalismanBagPacket data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer) {
                serverPlayer.openMenu(new SimpleMenuProvider(
                        (containerId, inventory, _) -> new TalismansMenu(
                                containerId, inventory, serverPlayer.getData(UnshatteredAttachments.PLAYER_TALISMAN_STORAGE)
                        ),
                        Component.translatable("container.unshattered.talisman_bag")
                ));
            }
        });
    }
}
