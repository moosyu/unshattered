package io.github.moosyu.items;

import com.google.common.collect.ImmutableSet;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.gui.menus.SackMenu;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;
import java.util.List;

public class SackItem extends Item {
    public final ImmutableSet<Item> acceptedItems;
    public final int sackSlotCapacity;

    /**
     * @param sackSlotCapacity capacity per slot of the sack
     */
    public SackItem(Properties properties, ImmutableSet<Item> acceptedItems, int sackSlotCapacity) {
        super(properties.stacksTo(1).component(UnshatteredDataComponents.SACK_CONTENTS.get(), new HashMap<>()));

        this.acceptedItems = acceptedItems;
        this.sackSlotCapacity = sackSlotCapacity;
    }

    @Override
    public boolean overrideOtherStackedOnMe(@NonNull ItemStack sack, @NonNull ItemStack other, @NonNull Slot slot, @NonNull ClickAction clickAction, @NonNull Player player, @NonNull SlotAccess carriedItem) {
        if (clickAction == ClickAction.SECONDARY) {
            if (!player.level().isClientSide() && player instanceof ServerPlayer serverPlayer) {
                List<Item> items = List.copyOf(acceptedItems);
                serverPlayer.openMenu(new SimpleMenuProvider(
                                (containerId, inventory, _) -> new SackMenu(containerId, inventory, sack, items, sackSlotCapacity),
                                sack.getItemName()
                        ),
                        buf -> {
                            SackMenu.ITEMS_STREAM_CODEC.encode(buf, items);
                            buf.writeVarInt(sackSlotCapacity);
                        }
                );
            }
            return true;
        }
        return false;
    }
}
