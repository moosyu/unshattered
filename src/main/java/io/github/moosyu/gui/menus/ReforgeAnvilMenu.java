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
    private final Container container;
    private final Inventory inventory;

    public ReforgeAnvilMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(3));
    }

    public ReforgeAnvilMenu(int containerId, Inventory inventory, Container container) {
        super(UnshatteredMenus.REFORGE_ANVIL_MENU_TYPE.get(), containerId);

        this.container = container;
        this.inventory = inventory;

        for (int i = 0; i < 3; i++) {
            this.addSlot(new Slot(container, i, 40 + (i * 10), 40));
        }
        this.addStandardInventorySlots(inventory, 8, 84);
        this.addInventoryHotbarSlots(inventory, 8, 142);
    }

    @Override
    public @NonNull ItemStack quickMoveStack(@NonNull Player player, int slotIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(@NonNull Player player) {
        return true;
    }
}
