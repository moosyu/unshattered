package io.github.moosyu.events;

import io.github.moosyu.packets.ClientsidePlayerSoundEffectPacket;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.TriState;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class ItemEntityPickupHandler {
    @SubscribeEvent
    public static void onItemEntityPickup(ItemEntityPickupEvent.Pre event) {
        ItemEntity itemEntity = event.getItemEntity();
        if (itemEntity.hasPickUpDelay()) {
            return;
        }

        ItemStack itemStack = itemEntity.getItem();
        if (itemStack.isEmpty()) {
            return;
        }

        Player player = event.getPlayer();
        int before = itemStack.getCount();
        int remaining = UnshatteredUtils.addItemStackToSack(player, itemStack);
        if (remaining == before) {
            return;
        }

        if (remaining <= 0) {
            itemStack.setCount(0);
            event.setCanPickup(TriState.TRUE);
        } else {
            itemStack.setCount(remaining);
        }

        if (player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new ClientsidePlayerSoundEffectPacket(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.ITEM_PICKUP), 0.5f, 2.0f * ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7f + 1.0f)));
        }
    }
}