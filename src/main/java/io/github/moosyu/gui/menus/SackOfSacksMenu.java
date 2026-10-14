package io.github.moosyu.gui.menus;

import io.github.moosyu.abilities.AbilityContext;
import io.github.moosyu.abilities.AbilityContextKey;
import io.github.moosyu.abilities.AbilityItem;
import io.github.moosyu.data.UnshatteredDataMaps;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.gui.menus.containers.SackOfSacksContainer;
import io.github.moosyu.gui.menus.containers.TalismanContainer;
import io.github.moosyu.items.ItemType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

public class SackOfSacksMenu extends AbstractContainerMenu {
    private final int ROWS = 3;
    private final int COLUMNS = 9;
    private final ItemStack[] lastKnownStacks = new ItemStack[SackOfSacksContainer.SACK_SLOTS];
    private final Inventory playerInventory;
    private final Container container;

    // client side
    public SackOfSacksMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(SackOfSacksContainer.SACK_SLOTS));
    }

    // server side (also now used by client, with a dummy container)
    public SackOfSacksMenu(int containerId, Inventory playerInventory, Container container) {
        super(UnshatteredMenus.SACK_OF_SACKS_MENU_TYPE.get(), containerId);

        checkContainerSize(container, container.getContainerSize());
        this.container = container;
        this.playerInventory = playerInventory;

        for (int i = 0; i < container.getContainerSize(); i++) {
            lastKnownStacks[i] = container.getItem(i).copy();
        }

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                addSlot(new Slot(container, col + row * 9, 8 + col * 18, 18 + row * 18) {
                    @Override
                    public boolean mayPlace(@NonNull ItemStack itemStack) {
                        if (itemStack.typeHolder().getData(UnshatteredDataMaps.ITEM_TYPE_DATA) == ItemType.SACK) {
                            Item placingItem = itemStack.getItem();
                            for (int i = 0; i < container.getContainerSize(); i++) {
                                if (container.getItem(i).is(placingItem)) return false;
                            }
                            return true;
                        }
                        return false;
                    }
                });
            }
        }

        addStandardInventorySlots(playerInventory, 8, 84);
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();

        Player player = playerInventory.player;
        if (player.level().isClientSide() || !(player instanceof ServerPlayer serverPlayer)) return;

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack current = container.getItem(i);
            ItemStack previous = lastKnownStacks[i];

            if (!ItemStack.isSameItemSameComponents(previous, current)) {
                lastKnownStacks[i] = current.copy();
            }
        }
    }

    @Override
    public @NonNull ItemStack quickMoveStack(@NonNull Player player, int slotIndex) {
        // taken from ChestMenu
        ItemStack clicked = ItemStack.EMPTY;
        Slot slot = slots.get(slotIndex);

        if (slot.hasItem()) {
            ItemStack itemStack = slot.getItem();
            clicked = itemStack.copy();
            if (slotIndex < ROWS * COLUMNS) {
                if (!moveItemStackTo(itemStack, ROWS * COLUMNS, slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(itemStack, 0, ROWS * COLUMNS, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack.isEmpty()) {
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