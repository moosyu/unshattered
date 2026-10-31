package io.github.moosyu.gui.menus;

import io.github.moosyu.data.components.UnshatteredDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;
import java.util.List;

public class SackMenu extends AbstractContainerMenu {
    public static final StreamCodec<RegistryFriendlyByteBuf, List<Item>> ITEMS_STREAM_CODEC = ByteBufCodecs.registry(Registries.ITEM).apply(ByteBufCodecs.list());
    public static final int SLOTS_PER_ROW = 10;
    public static final int TOP_HEIGHT = 17;
    public static final int BORDER_HEIGHT = 1;
    public static final int BOTTOM_HEIGHT = 97;
    public final int sackSlots;
    private final ItemStack sack;
    public final List<Item> acceptedItems;
    public final ContainerData counts;
    public final int sackSlotCapacity;

    public SackMenu(int containerId, Inventory inventory, RegistryFriendlyByteBuf buf) {
        this(containerId, inventory, ItemStack.EMPTY, ITEMS_STREAM_CODEC.decode(buf), buf.readVarInt(), true);
    }

    public SackMenu(int containerId, Inventory inventory, ItemStack sack, List<Item> acceptedItems, int sackSlotCapacity) {
        this(containerId, inventory, sack, acceptedItems, sackSlotCapacity, false);
    }

    private SackMenu(int containerId, Inventory inventory, ItemStack sack, List<Item> acceptedItems, int sackSlotCapacity, boolean client) {
        super(UnshatteredMenus.SACK_MENU_TYPE.get(), containerId);

        this.sack = sack;
        this.acceptedItems = acceptedItems;
        this.sackSlotCapacity = sackSlotCapacity;
        this.sackSlots = acceptedItems.size();
        this.counts = client ? new SimpleContainerData(sackSlots) : new ContainerData() {
            @Override public int get(int index) {
                return getStored(sack, acceptedItems.get(index));
            }

            @Override public void set(int index, int value) {}

            @Override public int getCount() {
                return acceptedItems.size();
            }
        };

        addDataSlots(counts);
        addStandardInventorySlots(inventory, 8, TOP_HEIGHT + getMiddleHeight(sackSlots) + BORDER_HEIGHT + 15);
    }

    private static HashMap<Holder<Item>, Integer> contents(ItemStack sack) {
        HashMap<Holder<Item>, Integer> map = sack.get(UnshatteredDataComponents.SACK_CONTENTS.get());

        return map != null ? map : new HashMap<>();
    }

    private static int getStored(ItemStack sack, Item item) {
        return contents(sack).getOrDefault(BuiltInRegistries.ITEM.wrapAsHolder(item), 0);
    }

    @Override
    public boolean clickMenuButton(@NonNull Player player, int id) {
        if (id == acceptedItems.size()) {
            if (sack.isEmpty()) {
                return false;
            }

            HashMap<Holder<Item>, Integer> updated = new HashMap<>(contents(sack));
            boolean changed = false;

            for (Item item : acceptedItems) {
                Holder<Item> holder = BuiltInRegistries.ITEM.wrapAsHolder(item);
                int space = sackSlotCapacity - updated.getOrDefault(holder, 0);

                for (int i = 0; i < Inventory.INVENTORY_SIZE && space > 0; i++) {
                    ItemStack itemStack = player.getInventory().getItem(i);

                    if (itemStack.is(item)) {
                        int moved = Math.min(itemStack.getCount(), space);
                        itemStack.shrink(moved);
                        space -= moved;
                        updated.merge(holder, moved, Integer::sum);
                        changed = true;
                    }
                }
            }

            if (changed) {
                sack.set(UnshatteredDataComponents.SACK_CONTENTS.get(), updated);
            }

            return changed;
        }

        if (id < 0 || id >= acceptedItems.size() || sack.isEmpty()) {
            return false;
        }

        Item item = acceptedItems.get(id);
        int stored = getStored(sack, item);
        if (stored <= 0) {
            return false;
        }

        int remaining = stored;
        while (remaining > 0) {
            ItemStack itemStack = new ItemStack(item, Math.min(remaining, item.getDefaultMaxStackSize()));
            int before = itemStack.getCount();
            player.getInventory().add(itemStack);

            int inserted = before - itemStack.getCount();
            if (inserted <= 0) {
                break;
            }
            remaining -= inserted;
        }

        if (remaining == stored) {
            return false;
        }

        HashMap<Holder<Item>, Integer> updated = new HashMap<>(contents(sack));

        if (remaining <= 0) {
            updated.remove(BuiltInRegistries.ITEM.wrapAsHolder(item));
        } else {
            updated.put(BuiltInRegistries.ITEM.wrapAsHolder(item), remaining);
        }

        sack.set(UnshatteredDataComponents.SACK_CONTENTS.get(), updated);

        return true;
    }

    @Override
    public boolean stillValid(@NonNull Player player) {
        return true;
    }

    public static int getMiddleHeight(int sackSlots) {
        return Math.ceilDiv(sackSlots, SLOTS_PER_ROW) * 16 + BORDER_HEIGHT;
    }

    @Override
    public @NonNull ItemStack quickMoveStack(@NonNull Player player, int slotIndex) {
        return ItemStack.EMPTY;
    }
}