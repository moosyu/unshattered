package io.github.moosyu.gui.menus;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

public class ForgeMenu extends AbstractContainerMenu {
    public ForgeMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, new SimpleContainer());
    }

    public ForgeMenu(int containerId, Inventory inventory, Container container) {
        super(UnshatteredMenus.FORGE_MENU_TYPE.get(), containerId);

        addStandardInventorySlots(inventory, 8, 121);
    }

    @Override
    public @NonNull ItemStack quickMoveStack(@NonNull Player player, int slotIndex) {
        ItemStack copy = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);

        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            copy = stack.copy();

            // inventory to hotbar
            if (slotIndex < 27) {
                if (!this.moveItemStackTo(stack, 27, 36, false)) {
                    return ItemStack.EMPTY;
                }
            // hotbar to inventory
            } else {
                if (!this.moveItemStackTo(stack, 0, 27, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack.getCount() == copy.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stack);
        }

        return copy;
    }

    @Override
    public boolean stillValid(@NonNull Player player) {
        return true;
    }
}
