package io.github.moosyu.gui.menus;

import io.github.moosyu.data.attachments.UnshatteredAttachments;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StorageMenu extends AbstractContainerMenu {
    private static final int COLUMNS = 9;
    private static final int VISIBLE_ROWS = StorageScreen.STORAGE_SLOTS_AREA_HEIGHT / 18;
    private final WindowContainer WINDOW = new WindowContainer(VISIBLE_ROWS * COLUMNS);
    private final Container fullStorage;
    // supposedly indices is the plural of index, who knew
    private final List<Integer> displayedIndices = new ArrayList<>();
    private String previousSearchInput = "";
    private boolean isSearching = false;
    public int currentPageIndex = 0;
    private final Player player;

    public StorageMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(StorageContainer.STORAGE_SLOTS));
    }

    public StorageMenu(int containerId, Inventory inventory, Container fullStorage) {
        super(UnshatteredMenus.STORAGE_MENU_TYPE.get(), containerId);

        player = inventory.player;
        this.fullStorage = fullStorage;

        for (int i = 0; i < fullStorage.getContainerSize(); i++) {
            displayedIndices.add(i);
        }

        for (int row = 0; row < VISIBLE_ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                addSlot(new Slot(WINDOW, col + row * COLUMNS, (col * 18) + 8, (row * 18) - 38) {
                    @Override
                    public boolean mayPlace(@NonNull ItemStack stack) {
                        if (!isSearching) return true;

                        return !WINDOW.getItem(getSlotIndex()).isEmpty();
                    }
                });
            }
        }

        refreshWindow();

        addStandardInventorySlots(inventory, 8, 84);
        addInventoryHotbarSlots(inventory, 8, 142);
    }

    private void refreshWindow() {
        for (int i = 0; i < WINDOW.getContainerSize(); i++) {
            int backing = WINDOW.getDisplayIndex(i);
            ItemStack stack = (backing >= 0 && backing < fullStorage.getContainerSize())
                    ? fullStorage.getItem(backing) : ItemStack.EMPTY;
            WINDOW.setItemDirect(i, stack);
        }
    }

    public void updatePage(boolean increment) {
        int totalPages = getTotalPages();
        if (increment && (currentPageIndex + 1) < totalPages) {
            currentPageIndex++;
        } else if (!increment && (currentPageIndex - 1) >= 0) {
            currentPageIndex--;
        } else {
            return;
        }

        // the client's fullStorage is empty at the moment so without this guard everything just looks empty
        if (!player.level().isClientSide()) {
            refreshWindow();
        }
    }

    public int getTotalPages() {
        if (isSearching) {
            return Math.max(1, Mth.ceil(displayedIndices.size() / (double) WINDOW.getContainerSize()));
        } else {
            return player.getData(UnshatteredAttachments.PLAYER_BANK_PAGES);
        }
    }

    public void handleSearch(String input) {
        if (input.equals(previousSearchInput)) return;
        previousSearchInput = input;
        isSearching = !input.isEmpty();

        displayedIndices.clear();
        if (!isSearching) {
            for (int i = 0; i < fullStorage.getContainerSize(); i++) {
                displayedIndices.add(i);
            }
        } else {
            for (int i = 0; i < fullStorage.getContainerSize(); i++) {
                ItemStack stack = fullStorage.getItem(i);
                if (!stack.isEmpty() && stack.getHoverName().getString().toLowerCase(Locale.ROOT).contains(input.toLowerCase(Locale.ROOT))) {
                    displayedIndices.add(i);
                }
            }
        }

        currentPageIndex = 0;
        refreshWindow();
    }

    private int getTotalRows() {
        return Mth.ceil(displayedIndices.size() / (double) COLUMNS);
    }

    @Override
    public @NonNull ItemStack quickMoveStack(@NonNull Player player, int slotIndex) {
        ItemStack clicked = ItemStack.EMPTY;
        Slot slot = slots.get(slotIndex);
        final int windowSlotCount = WINDOW.getContainerSize();

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

    // contains the currently visible slice of fullStorage (or the filtered view)
    private class WindowContainer extends SimpleContainer {
        WindowContainer(int size) {
            super(size);
        }

        @Override
        public void setItem(int windowIndex, @NonNull ItemStack stack) {
            int backing = getDisplayIndex(windowIndex);
            super.setItem(windowIndex, stack);

            if (backing < 0 || backing >= fullStorage.getContainerSize()) return;
            fullStorage.setItem(backing, stack);

            // remove fully withdrawn stacks from view
            if (isSearching && stack.isEmpty()) {
                int index = displayedIndices.indexOf(backing);
                if (index >= 0) {
                    displayedIndices.remove(index);
                    refreshWindow();
                }
            }
        }

        void setItemDirect(int windowIndex, ItemStack stack) {
            super.setItem(windowIndex, stack);
        }

        private int getDisplayIndex(int windowIndex) {
            int displayIndex = windowIndex + currentPageIndex * getContainerSize();
            if (displayIndex < 0 || displayIndex >= displayedIndices.size()) return -1;
            return displayedIndices.get(displayIndex);
        }
    }
}