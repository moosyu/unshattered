package io.github.moosyu.util;

import io.github.moosyu.attachments.PlayerCollectionsAttachment;
import io.github.moosyu.attachments.UnshatteredAttachments;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class CollectionUtil {
    /**
     * adds items to collection while checking to make sure the item isn't empty or missing the collectable data component
     * @param player player getting the item
     * @param itemStack the item being acquired
     */
    public static void addItemToCollection(Player player, ItemStack itemStack) {
        if (itemStack.isEmpty()
                || itemStack.count() < 1
                || itemStack.getOrDefault(UnshatteredDataComponents.ITEM_COLLECTABLE.get(), false)
        ) return;

        PlayerCollectionsAttachment collections = player.getData(UnshatteredAttachments.PLAYER_COLLECTIONS.get());

        collections.addPickedUpItem(itemStack);
        player.syncData(UnshatteredAttachments.PLAYER_COLLECTIONS);
    }
}
