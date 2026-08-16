package io.github.moosyu.util;

import io.github.moosyu.data.attachments.PlayerCollectionsAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.collectables.CollectableEntries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class CollectionUtil {
    /**
     * adds items to collection while checking to make sure the item isn't empty or missing the collectable data component
     * @param player player getting the item
     * @param itemStack the item being acquired
     */    public static void addItemToCollection(Player player, ItemStack itemStack) {
        if (itemStack.isEmpty()
                || itemStack.count() < 1
                || CollectableEntries.getCollectableEntry(itemStack.typeHolder()) == null
        ) return;

        PlayerCollectionsAttachment collections = player.getData(UnshatteredAttachments.PLAYER_COLLECTIONS.get());

        collections.addPickedUpItem(itemStack, player);
    }

    /**
     * should be used instead of Inventory#add when adding items that were harvested by the player
     * @param player player having the item added
     * @param itemStack itemstack being added to inventory
     */
    public static void givePlayerHarvestedItemStack(Player player, ItemStack itemStack) {
        addItemToCollection(player, itemStack);
        player.getInventory().add(itemStack);
        player.syncData(UnshatteredAttachments.PLAYER_COLLECTIONS);
    }
}
