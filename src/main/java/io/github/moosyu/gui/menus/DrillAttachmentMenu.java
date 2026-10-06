package io.github.moosyu.gui.menus;

import io.github.moosyu.data.UnshatteredDataMaps;
import io.github.moosyu.data.components.ItemAttachments;
import io.github.moosyu.data.components.ItemFuel;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.ItemType;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

public class DrillAttachmentMenu extends AbstractContainerMenu {
    private static final ItemAttachments.SlotType[] ATTACHMENT_TYPES = {
            ItemAttachments.SlotType.FUEL_TANK, ItemAttachments.SlotType.DRILL_ENGINE, ItemAttachments.SlotType.UPGRADE_MODULE
    };

    private static final int DRILL_SLOT_INDEX = ATTACHMENT_TYPES.length;
    private static final int FUEL_SLOT_INDEX = DRILL_SLOT_INDEX + 1;
    public static final int SLOTS = FUEL_SLOT_INDEX + 1;
    public static final int COMBINE_FUEL_BUTTON = 0;

    private final Player player;
    private final SimpleContainer container = new SimpleContainer(SLOTS) {
        @Override
        public void setChanged() {
            super.setChanged();

            if (updating) {
                return;
            }

            updating = true;

            ItemStack drill = container.getItem(DRILL_SLOT_INDEX);
            if (drill.isEmpty()) {
                for (int i = 0; i < ATTACHMENT_TYPES.length; i++) {
                    container.setItem(i, ItemStack.EMPTY);
                }

                loadedDrill = ItemStack.EMPTY;

                ItemStack fuel = container.getItem(FUEL_SLOT_INDEX);
                if (!fuel.isEmpty()) {
                    container.setItem(FUEL_SLOT_INDEX, ItemStack.EMPTY);
                    if (!player.getInventory().add(fuel)) {
                        player.drop(fuel, false);
                    }
                }
            } else if (drill != loadedDrill) {
                loadedDrill = drill;
                ItemAttachments attachments = drill.get(UnshatteredDataComponents.ITEM_ATTACHMENTS.get());

                for (int i = 0; i < ATTACHMENT_TYPES.length; i++) {
                    container.setItem(i, attachments == null ? ItemStack.EMPTY : attachments.get(ATTACHMENT_TYPES[i]));
                }
            // this branch is for the actual attachments, clientside is only checked here so there isnt an odd delay
            // when the drill is added or removed but that wouldn't occur in this situation
            } else if (!player.level().isClientSide() && player instanceof ServerPlayer) {
                ItemAttachments current = drill.get(UnshatteredDataComponents.ITEM_ATTACHMENTS.get());
                if (current != null) {
                    ItemAttachments updated = current;
                    for (int i = 0; i < ATTACHMENT_TYPES.length; i++) {
                        ItemStack previous = updated.get(ATTACHMENT_TYPES[i]);
                        ItemStack next = container.getItem(i);

                        if ((previous.isEmpty() && next.isEmpty()) || ItemStack.matches(previous, next)) {
                            continue;
                        }

                        Map<ItemAttachments.SlotType, ItemStack> copy = new EnumMap<>(ItemAttachments.SlotType.class);
                        copy.putAll(updated.attachments());
                        copy.put(ATTACHMENT_TYPES[i], next);
                        updated = new ItemAttachments(copy);
                    }

                    if (updated != current) {
                        drill.set(UnshatteredDataComponents.ITEM_ATTACHMENTS.get(), updated);

                        ItemFuel itemFuel = drill.get(UnshatteredDataComponents.FUEL.get());
                        if (itemFuel != null) {
                            int maxFuel = itemFuel.maxFuel();
                            if (itemFuel.currentFuel() > maxFuel)  {
                                drill.set(UnshatteredDataComponents.FUEL.get(), new ItemFuel(maxFuel, maxFuel));
                            }
                        }
                    }
                }
            }

            updating = false;
        }
    };

    private ItemStack loadedDrill = ItemStack.EMPTY;
    private boolean updating;

    public DrillAttachmentMenu(int containerId, Inventory playerInventory) {
        super(UnshatteredMenus.DRILL_ATTACHMENT_MENU_TYPE.get(), containerId);

        player = playerInventory.player;

        for (int i = 0; i < ATTACHMENT_TYPES.length; i++) {
            addSlot(new AttachmentSlot(container, i, 56 + i * 24, 24, ATTACHMENT_TYPES[i]));
        }

        addSlot(new Slot(container, DRILL_SLOT_INDEX, 56, 55) {
            @Override
            public boolean mayPlace(@NonNull ItemStack itemStack) {
                return itemStack.typeHolder().getData(UnshatteredDataMaps.ITEM_TYPE_DATA) == ItemType.DRILL;
            }

            @Override
            public Identifier getNoItemIcon() {
                return UnshatteredUtils.getUnshatteredIdentifier("slots/drill_outline");
            }
        });

        addSlot(new Slot(container, FUEL_SLOT_INDEX, 104, 55) {
            @Override
            public boolean mayPlace(@NonNull ItemStack itemStack) {
                SlotAccess drillSlotAccess = container.getSlot(DRILL_SLOT_INDEX);
                return UnshatteredUtils.getItemFuelAmount(itemStack) > 0 && drillSlotAccess != null && !drillSlotAccess.get().isEmpty();
            }

            @Override
            public Identifier getNoItemIcon() {
                return UnshatteredUtils.getUnshatteredIdentifier("slots/fuel_outline");
            }
        });

        addStandardInventorySlots(playerInventory, 8, 84);
    }

    @Override
    public @NonNull ItemStack quickMoveStack(@NonNull Player player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack itemStack = slot.getItem();
        ItemStack original = itemStack.copy();

        if (index < SLOTS) {
            if (!moveItemStackTo(itemStack, SLOTS, slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else if (!moveItemStackTo(itemStack, 0, SLOTS, false)) {
            return ItemStack.EMPTY;
        }

        if (itemStack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        if (itemStack.getCount() == original.getCount()) {
            return ItemStack.EMPTY;
        }

        slot.onTake(player, itemStack);
        return original;
    }

    @Override
    public boolean canTakeItemForPickAll(@NonNull ItemStack itemStack, @NonNull Slot slot) {
        return slot.container != container;
    }

    @Override
    public void removed(@NonNull Player player) {
        super.removed(player);
        if (player.level().isClientSide()) {
            return;
        }

        for (int i = 0; i < ATTACHMENT_TYPES.length; i++) {
            container.removeItemNoUpdate(i);
        }

        clearContainer(player, container);
    }

    @Override
    public boolean stillValid(@NonNull Player player) {
        return true;
    }

    private static class AttachmentSlot extends Slot {
        private final ItemAttachments.SlotType type;

        AttachmentSlot(Container container, int index, int x, int y, ItemAttachments.SlotType type) {
            super(container, index, x, y);
            this.type = type;
        }

        @Override
        public boolean mayPlace(@NonNull ItemStack itemStack) {
            if (itemStack.typeHolder().getData(UnshatteredDataMaps.ITEM_TYPE_DATA) != type.correspondingType) {
                return false;
            }

            ItemAttachments attachments = container.getItem(DRILL_SLOT_INDEX).get(UnshatteredDataComponents.ITEM_ATTACHMENTS.get());
            return attachments != null && attachments.hasSlot(type);
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }

        public @Nullable Identifier getNoItemIcon() {
            return type.noItemIcon;
        }
    }

    @Override
    public boolean clickMenuButton(@NonNull Player player, int id) {
        if (id == COMBINE_FUEL_BUTTON) {
            combineFuel();
            return true;
        }
        return super.clickMenuButton(player, id);
    }

    private void combineFuel() {
        ItemStack drill = container.getItem(DRILL_SLOT_INDEX);
        ItemStack fuelItem = container.getItem(FUEL_SLOT_INDEX);
        int fuelPerItem = UnshatteredUtils.getItemFuelAmount(fuelItem);
        ItemFuel drillFuel = drill.get(UnshatteredDataComponents.FUEL.get());

        if (drill.isEmpty() || fuelItem.isEmpty() || drillFuel == null || fuelPerItem <= 0) {
            return;
        }

        int effectiveMax = drillFuel.getMaxFuel(drill);
        int currentFuel = drillFuel.currentFuel();
        int needed = effectiveMax - currentFuel;
        if (needed <= 0) {
            return;
        }

        int itemsUsed = Math.min(fuelItem.getCount(), Math.ceilDiv(needed, fuelPerItem));

        drill.set(UnshatteredDataComponents.FUEL.get(), new ItemFuel(drillFuel.maxFuel(), Math.min(effectiveMax, currentFuel + itemsUsed * fuelPerItem)));
        container.removeItem(FUEL_SLOT_INDEX, itemsUsed);
    }
}