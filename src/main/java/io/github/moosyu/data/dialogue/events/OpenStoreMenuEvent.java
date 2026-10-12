package io.github.moosyu.data.dialogue.events;

import com.mojang.serialization.MapCodec;
import io.github.moosyu.Unshattered;
import io.github.moosyu.data.UnshatteredDataMaps;
import io.github.moosyu.data.VendorItem;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.dialogue.DialogueTriggeredEvent;
import io.github.moosyu.events.DataPackRegistryHandler;
import io.github.moosyu.gui.menus.VendorMenu;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.EntityType;

import java.util.List;

public record OpenStoreMenuEvent(EntityType<?> npcShopEntityType) implements DialogueTriggeredEvent {
    @Override
    public void trigger(ServerPlayer player) {
        List<VendorItem> vendorItems = BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(npcShopEntityType).getData(UnshatteredDataMaps.VENDOR_SHOP_DATA);

        if (vendorItems == null) {
            Unshattered.LOGGER.warn("no shop stock found for {}", npcShopEntityType);
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

    public static MapCodec<OpenStoreMenuEvent> CODEC = EntityType.CODEC.fieldOf("npc_shop_identifier").xmap(OpenStoreMenuEvent::new, OpenStoreMenuEvent::npcShopEntityType);
    public static StreamCodec<RegistryFriendlyByteBuf, OpenStoreMenuEvent> STREAM_CODEC = EntityType.STREAM_CODEC.map(OpenStoreMenuEvent::new, OpenStoreMenuEvent::npcShopEntityType);
}
