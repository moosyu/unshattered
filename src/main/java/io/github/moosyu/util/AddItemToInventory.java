package io.github.moosyu.util;

import io.github.moosyu.attachments.PlayerCollectionsAttachment;
import io.github.moosyu.attachments.UnshatteredAttachments;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class AddItemToInventory {
    /**
     * should be used instead of Inventory#add as it adds the item to collection too
     * @param player player having the item added
     * @param itemStack itemstack being added to inventory
     */
    public static void addItemToInventory(Player player, ItemStack itemStack) {
        PlayerCollectionsAttachment collections = player.getData(UnshatteredAttachments.PLAYER_COLLECTIONS.get());

        collections.addPickedUpItem(itemStack);
        player.getInventory().add(itemStack);
        player.syncData(UnshatteredAttachments.PLAYER_COLLECTIONS);
    }
}
