package io.github.moosyu.data.recipes;

import io.github.moosyu.Unshattered;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jspecify.annotations.NonNull;

import java.util.List;

public record ForgeRecipeInput(List<ItemStack> inputs) implements RecipeInput {
    @Override
    public @NonNull ItemStack getItem(int slot) {
        if (slot >= inputs.size()) {
            Unshattered.LOGGER.error("forge recipe input doesn't have an itemstack in index {}", slot);
            return ItemStack.EMPTY;
        }
        return inputs.get(slot);
    }

    @Override
    public int size() {
        return inputs.size();
    }

    public static ForgeRecipeInput getRecipeInput(Player player) {
        return new ForgeRecipeInput(player.getInventory().getNonEquipmentItems());
    }
}
