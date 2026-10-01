package io.github.moosyu.gui.menus;

import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.ItemType;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

public class DrillAttachmentMenu extends AbstractContainerMenu {
    public static final int SLOTS = 4;

    private final Inventory playerInventory;
    private final Container container;

    public DrillAttachmentMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(SLOTS));
    }

    public DrillAttachmentMenu(int containerId, Inventory playerInventory, Container container) {
        super(UnshatteredMenus.DRILL_ATTACHMENT_MENU_TYPE.get(), containerId);

        checkContainerSize(container, container.getContainerSize());
        this.container = container;
        this.playerInventory = playerInventory;

        addSlot(new Slot(container, 0, 51, 26) {
            @Override
            public boolean mayPlace(@NonNull ItemStack itemStack) {
                return itemStack.get(UnshatteredDataComponents.ITEM_TYPE.get()) == ItemType.FUEL_TANK;
            }
        });
        addSlot(new Slot(container, 1, 80, 26) {
            @Override
            public boolean mayPlace(@NonNull ItemStack itemStack) {
                return itemStack.get(UnshatteredDataComponents.ITEM_TYPE.get()) == ItemType.DRILL_ENGINE;
            }
        });
        addSlot(new Slot(container, 2, 109, 26) {
            @Override
            public boolean mayPlace(@NonNull ItemStack itemStack) {
                return itemStack.get(UnshatteredDataComponents.ITEM_TYPE.get()) == ItemType.UPGRADE_MODULE;
            }
        });
        addSlot(new Slot(container, 3, 80, 50) {
            @Override
            public boolean mayPlace(@NonNull ItemStack itemStack) {
                return itemStack.get(UnshatteredDataComponents.ITEM_TYPE.get()) == ItemType.DRILL;
            }
        });

        addStandardInventorySlots(playerInventory, 8, 84);
        addInventoryHotbarSlots(playerInventory, 8, 142);
    }

//    @Override
//    public void broadcastChanges() {
//        super.broadcastChanges();
//
//        Player player = playerInventory.player;
//        if (player.level().isClientSide() || !(player instanceof ServerPlayer serverPlayer)) return;
//    }

    @Override
    public @NonNull ItemStack quickMoveStack(@NonNull Player player, int slotIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(@NonNull Player player) {
        return true;
    }
}