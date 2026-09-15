package io.github.moosyu.gui.menus;

import io.github.moosyu.gui.menus.containers.StorageContainer;
import io.github.moosyu.gui.screens.StorageScreen;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

public class StorageMenu extends AbstractContainerMenu {
    final Inventory inventory;
    final Container fullStorage;
    private static final int COLUMNS = 8;
    private static final int VISIBLE_ROWS = StorageScreen.STORAGE_SLOTS_AREA_HEIGHT / 18;
    private final int totalRows = StorageContainer.STORAGE_SLOTS / COLUMNS;
    private final WindowContainer window = new WindowContainer(VISIBLE_ROWS * COLUMNS);
    private int scrollRows = 0;

    public StorageMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(StorageContainer.STORAGE_SLOTS));
    }

    public StorageMenu(int containerId, Inventory inventory, Container fullStorage) {
        super(UnshatteredMenus.STORAGE_MENU_TYPE.get(), containerId);
        this.fullStorage = fullStorage;
        this.inventory = inventory;

        for (int row = 0; row < VISIBLE_ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                addSlot(new Slot(window, col + row * COLUMNS, (col * 18) + 8, (row * 18) - 38));
            }
        }

        refreshWindow();

        this.addStandardInventorySlots(inventory, 8, 84);
        this.addInventoryHotbarSlots(inventory, 8, 142);
    }

    private int toBackingIndex(int windowIndex) {
        int col = windowIndex % COLUMNS;
        int row = windowIndex / COLUMNS;
        return col + (row + scrollRows) * COLUMNS;
    }

    private void refreshWindow() {
        for (int i = 0; i < window.getContainerSize(); i++) {
            int backing = toBackingIndex(i);
            ItemStack stack = (backing >= 0 && backing < fullStorage.getContainerSize()) ? fullStorage.getItem(backing) : ItemStack.EMPTY;
            window.setItemDirect(i, stack);
        }
    }

    public void handleScroll(boolean scrolledDown) {
        int maxScroll = Math.max(0, totalRows - VISIBLE_ROWS);
        int next = Mth.clamp(scrollRows + (scrolledDown ? 1 : -1), 0, maxScroll);
        if (next == scrollRows) return;
        scrollRows = next;
        refreshWindow();
    }

    @Override
    public @NonNull ItemStack quickMoveStack(@NonNull Player player, int slotIndex) {
        ItemStack clicked = ItemStack.EMPTY;
        Slot slot = slots.get(slotIndex);
        final int windowSlotCount = window.getContainerSize();

        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            clicked = stack.copy();
            if (slotIndex < windowSlotCount) {
                if (!moveItemStackTo(stack, windowSlotCount, slots.size(), true)) return ItemStack.EMPTY;
            } else if (!moveItemStackTo(stack, 0, windowSlotCount, false)) {
                return ItemStack.EMPTY;
            }
            if (stack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return clicked;
    }

    @Override
    public boolean stillValid(@NonNull Player player) {
        return true;
    }

    private class WindowContainer extends SimpleContainer {
        WindowContainer(int size) {
            super(size);
        }

        @Override
        public void setItem(int windowIndex, @NonNull ItemStack stack) {
            super.setItem(windowIndex, stack);
            int backing = toBackingIndex(windowIndex);
            if (backing >= 0 && backing < fullStorage.getContainerSize()) {
                fullStorage.setItem(backing, stack);
            }
        }

        void setItemDirect(int windowIndex, ItemStack stack) {
            super.setItem(windowIndex, stack);
        }
    }
}