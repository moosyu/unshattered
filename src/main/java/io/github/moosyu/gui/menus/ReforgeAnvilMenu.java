package io.github.moosyu.gui.menus;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

public class ReforgeAnvilMenu extends AbstractContainerMenu {
    public ReforgeAnvilMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(3));
    }

    public ReforgeAnvilMenu(int containerId, Inventory inventory, Container container) {
        super(UnshatteredMenus.REFORGE_ANVIL_MENU_TYPE.get(), containerId);

        this.addSlot(new Slot(container, 1, 27, 36));
        this.addSlot(new Slot(container, 2, 76, 36));
        this.addSlot(new Slot(container, 0, 134, 36) {
            @Override
            public boolean mayPlace(@NonNull ItemStack stack) {
                return false;
            }

            @Override
            public void onTake(@NonNull Player player, @NonNull ItemStack stack) {
                container.getItem(0).shrink(1);
                container.getItem(1).shrink(1);
                container.setChanged();

                super.onTake(player, stack);
            }
        });

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

            if (!moveItemStackTo(stack, slotIndex, 0, true)) {
                return ItemStack.EMPTY;
            }
        }

        return clicked;
    }

    @Override
    public boolean stillValid(@NonNull Player player) {
        return true;
    }
}
