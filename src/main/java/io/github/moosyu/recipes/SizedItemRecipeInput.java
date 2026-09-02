package io.github.moosyu.recipes;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jspecify.annotations.NonNull;

import java.util.List;

public record SizedItemRecipeInput(List<ItemStack> itemStacks) implements RecipeInput {
    @Override
    public @NonNull ItemStack getItem(int slot) {
        return itemStacks.get(slot);
    }


    @Override
    public int size() {
        return Math.min(itemStacks.size(), 9);
    }
}
