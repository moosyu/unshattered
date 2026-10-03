package io.github.moosyu.gui.menus.containers;

import net.minecraft.core.NonNullList;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;

public class SellSlotContainer extends SimpleContainer implements ValueIOSerializable {
    ItemStack currentItemStack = ItemStack.EMPTY;

    public SellSlotContainer() {
        super(1);
    }

    @Override
    public void setChanged() {
        super.setChanged();

        ItemStack changedStack = getItem(0);
        if (changedStack != currentItemStack) {
            if (changedStack.isEmpty()) {

            } else {

            }
        }
    }

    @Override
    public void serialize(ValueOutput output) {
        output.store("items", NonNullList.codecOf(ItemStack.OPTIONAL_CODEC), getItems());
    }

    @Override
    public void deserialize(ValueInput input) {
        input.read("items", NonNullList.codecOf(ItemStack.OPTIONAL_CODEC)).ifPresent(items -> {
            getItems().set(0, items.getFirst());
        });
    }
}
