package io.github.moosyu.gui.menus;

import io.github.moosyu.gui.menus.containers.StorageContainer;
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
    final Container container;
    private final int ROWS = 6;
    private final int COLUMNS = 8;

    public StorageMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(StorageContainer.STORAGE_SLOTS));
    }

    public StorageMenu(int containerId, Inventory inventory, Container container) {
        super(UnshatteredMenus.STORAGE_MENU_TYPE.get(), containerId);

        this.container = container;
        this.inventory = inventory;

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                addSlot(new Slot(container, col + row * COLUMNS, (col * 18) + 8, (row * 18) - 38));
            }
        }

        this.addStandardInventorySlots(inventory, 8, 84);
        this.addInventoryHotbarSlots(inventory, 8, 142);
    }

    @Override
    public @NonNull ItemStack quickMoveStack(@NonNull Player player, int slotIndex) {
        ItemStack clicked = ItemStack.EMPTY;
        Slot slot = slots.get(slotIndex);

        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            clicked = stack.copy();
            if (slotIndex < ROWS * COLUMNS) {
                if (!moveItemStackTo(stack, ROWS * COLUMNS, slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(stack, 0, ROWS * COLUMNS, false)) {
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
}
