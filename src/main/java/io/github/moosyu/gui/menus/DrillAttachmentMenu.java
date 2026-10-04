package io.github.moosyu.gui.menus;

import io.github.moosyu.abilities.AbilityContext;
import io.github.moosyu.abilities.AbilityContextKey;
import io.github.moosyu.data.attachments.PlayerAbilityEffectsAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.components.ItemAttachments;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.ItemType;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
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

    private static final int DRILL_SLOT = ATTACHMENT_TYPES.length;
    public static final int SLOTS = DRILL_SLOT + 1;

    private final Player player;
    private final SimpleContainer container = new SimpleContainer(SLOTS) {
        @Override
        public void setChanged() {
            super.setChanged();

            if (updating) {
                return;
            }

            updating = true;

            ItemStack drill = container.getItem(DRILL_SLOT);
            if (drill.isEmpty()) {
                for (int i = 0; i < ATTACHMENT_TYPES.length; i++) {
                    container.setItem(i, ItemStack.EMPTY);
                }

                loadedDrill = ItemStack.EMPTY;
            } else if (drill != loadedDrill) {
                loadedDrill = drill;
                ItemAttachments attachments = drill.get(UnshatteredDataComponents.ITEM_ATTACHMENTS.get());

                for (int i = 0; i < ATTACHMENT_TYPES.length; i++) {
                    container.setItem(i, attachments == null ? ItemStack.EMPTY : attachments.get(ATTACHMENT_TYPES[i]));
                }
            // this branch is for the actual attachments, clientside is only checked here so there isnt an odd delay
            // when the drill is added or removed but that wouldn't occur in this situation
            } else if (!player.level().isClientSide() && player instanceof ServerPlayer serverPlayer) {
                ItemAttachments current = drill.get(UnshatteredDataComponents.ITEM_ATTACHMENTS.get());
                if (current != null) {
                    ItemAttachments updated = current;
                    for (int i = 0; i < ATTACHMENT_TYPES.length; i++) {
                        ItemStack previous = updated.get(ATTACHMENT_TYPES[i]);
                        ItemStack next = container.getItem(i);

                        if ((previous.isEmpty() && next.isEmpty()) || ItemStack.matches(previous, next)) {
                            continue;
                        }

                        PlayerAbilityEffectsAttachment abilities = player.getData(UnshatteredAttachments.PLAYER_ABILITIES.get());
                        AbilityContext context = new AbilityContext().add(AbilityContextKey.PLAYER, serverPlayer);

                        abilities.removePassiveItem(previous, context);
                        abilities.addPassiveItem(next, context);

                        Map<ItemAttachments.SlotType, ItemStack> copy = new EnumMap<>(ItemAttachments.SlotType.class);
                        copy.putAll(updated.attachments());
                        copy.put(ATTACHMENT_TYPES[i], next);
                        updated = new ItemAttachments(copy);
                    }

                    if (updated != current) {
                        drill.set(UnshatteredDataComponents.ITEM_ATTACHMENTS.get(), updated);
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
            addSlot(new AttachmentSlot(container, i, 51 + i * 29, 26, ATTACHMENT_TYPES[i]));
        }

        addSlot(new Slot(container, DRILL_SLOT, 80, 50) {
            @Override
            public boolean mayPlace(@NonNull ItemStack itemStack) {
                return itemStack.get(UnshatteredDataComponents.ITEM_TYPE.get()) == ItemType.DRILL;
            }

            @Override
            public Identifier getNoItemIcon() {
                return UnshatteredUtils.getUnshatteredIdentifier("slots/drill_outline");
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
            if (itemStack.get(UnshatteredDataComponents.ITEM_TYPE.get()) != type.correspondingType) {
                return false;
            }

            ItemAttachments attachments = container.getItem(DRILL_SLOT).get(UnshatteredDataComponents.ITEM_ATTACHMENTS.get());
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
}