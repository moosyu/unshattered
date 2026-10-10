package io.github.moosyu.data.dialogue.events;

import com.mojang.serialization.MapCodec;
import io.github.moosyu.Unshattered;
import io.github.moosyu.data.VendorItem;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.dialogue.DialogueTriggeredEvent;
import io.github.moosyu.events.DataPackRegistryHandler;
import io.github.moosyu.gui.menus.VendorMenu;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;

import java.util.List;

public record OpenStoreMenuEvent(Identifier npcShopIdentifier) implements DialogueTriggeredEvent {
    @Override
    public void trigger(ServerPlayer player) {
        List<VendorItem> vendorItems = player.registryAccess().lookupOrThrow(DataPackRegistryHandler.SHOP_STOCK_KEY).getValue(npcShopIdentifier);

        if (vendorItems == null) {
            Unshattered.LOGGER.warn("no shop stock found for {}", npcShopIdentifier);
            return;
        }

        player.openMenu(new SimpleMenuProvider((containerId, inventory, _) -> new VendorMenu(containerId, inventory, vendorItems, player.getData(UnshatteredAttachments.PLAYER_SELL_SLOT)),
                        Component.translatable("screen.unshattered.vendor")
                ),
                buf -> VendorItem.STREAM_CODEC.apply(ByteBufCodecs.list()).encode(buf, vendorItems)
        );
    }

    @Override
    public MapCodec<? extends DialogueTriggeredEvent> codec() {
        return CODEC;
    }

    public static MapCodec<OpenStoreMenuEvent> CODEC = Identifier.CODEC.fieldOf("npc_shop_identifier").xmap(OpenStoreMenuEvent::new, OpenStoreMenuEvent::npcShopIdentifier);
    public static StreamCodec<ByteBuf, OpenStoreMenuEvent> STREAM_CODEC = Identifier.STREAM_CODEC.map(OpenStoreMenuEvent::new, OpenStoreMenuEvent::npcShopIdentifier);
}
