package io.github.moosyu.gui.components;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

import java.util.function.IntSupplier;

public class ScrollableSlot extends Slot {
    private final Container backingContainer;
    private final int column;
    private final int initialRow;
    private final int columns;
    private final IntSupplier scrollOffsetRows;

    public ScrollableSlot(Container backingContainer, int column, int initialRow, int columns, int x, int y, IntSupplier scrollOffsetRows) {
        super(backingContainer, column + initialRow * columns, x, y);
        this.backingContainer = backingContainer;
        this.column = column;
        this.initialRow = initialRow;
        this.columns = columns;
        this.scrollOffsetRows = scrollOffsetRows;
    }

    private int backingIndex() {
        return column + (initialRow + scrollOffsetRows.getAsInt()) * columns;
    }

    private boolean inRange() {
        int index = backingIndex();
        return index >= 0 && index < backingContainer.getContainerSize();
    }

    @Override public boolean hasItem() {
        return inRange() && !backingContainer.getItem(backingIndex()).isEmpty();
    }

    @Override public @NonNull ItemStack getItem() {
        return inRange() ? backingContainer.getItem(backingIndex()) : ItemStack.EMPTY;
    }

    @Override public void set(@NonNull ItemStack stack) {
        if (inRange()) {
            backingContainer.setItem(backingIndex(), stack); setChanged();
        }
    }

    @Override public void setChanged() {
        if (inRange()) {
            backingContainer.setChanged();
        }
    }

    @Override public int getMaxStackSize() {
        return inRange() ? backingContainer.getMaxStackSize() : 64;

    }

    @Override public @NonNull ItemStack remove(int amount) {
        return inRange() ? backingContainer.removeItem(backingIndex(), amount) : ItemStack.EMPTY;
    }

    @Override public boolean mayPlace(@NonNull ItemStack stack) {
        return inRange() && backingContainer.canPlaceItem(backingIndex(), stack);
    }

    @Override public boolean mayPickup(@NonNull Player player) {
        return inRange();
    }

    @Override public boolean isActive() {
        return inRange();
    }
}